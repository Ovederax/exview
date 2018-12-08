package comp.items

import model.*
import react.*
import react.dom.*
import kotlinx.html.js.*
import rest.*
import model.*
import org.w3c.dom.*
import org.w3c.dom.events.Event
import kotlin.browser.window


class ExamenByAuditoriumEdit: RComponent<ExamenByAuditoriumEdit.Props, ExamenByAuditoriumEdit.State>(){
    var refs:dynamic = null
	
	interface Props: RProps {
        var jsonAuditorium: JsonAuditorium
		// sessionSubjects.auditorium == jsonAuditorium.name
		var sessionSubjects: List<JsonSessionSubject>
		var pojoSessionSubjects: Map<JsonSessionSubject,PojoSessionSubject>
		var studentGroups: List<JsonStudentGroup>
        var num: Int
		var allSessionSubjects: List<JsonSessionSubject>
		var onUpdate: (UpdateSessionSubject)->Unit
		var onDelete: (UpdateSessionSubject)->Unit
    }  

    interface State: RState {
        var showSubDialog: Boolean
		var sessionSubjectsNoDay: MutableList<JsonSessionSubject>
		var selectedDay: Int
		var sessionSubject: JsonSessionSubject?
		var selectedGroup: JsonStudentGroup?
		var studentGroupsList: MutableList<JsonStudentGroup>
		var sessionSubjectsNoDayByGroup: MutableList<JsonSessionSubject>
		var selectedSubjectBySessionSubjectNoDay: JsonSessionSubject?
    }
	
    init {
		state.showSubDialog = false
		state.selectedDay = 0
		state.selectedGroup = null
    }
	
	fun onClick(day: Int) {
		setState {
			selectedDay = day
			showSubDialog = true
		}
	}
	
    override fun RBuilder.render() {
		state.sessionSubjectsNoDay = ArrayList()
		state.studentGroupsList = ArrayList()
		state.sessionSubjectsNoDayByGroup = ArrayList()
		state.sessionSubject = null
		
		for(it in props.allSessionSubjects) {
			val pojo = props.pojoSessionSubjects.get(it) ?: PojoSessionSubject()
			if(it.date == -1) {
				state.sessionSubjectsNoDay.add(it)
			} else if(it.date == state.selectedDay && pojo.auditorium == props.jsonAuditorium.name) {
				state.sessionSubject = it
			}
		}
		val insSelectDay = state.sessionSubject
		
        tr {
            td { +props.jsonAuditorium.name }
            for (i in 7..30) {
				var b = false
				val day = i+1
				for(it in props.sessionSubjects) {
					if(it.date == day) {
						b = true
					}
				}
				var cl: String = "exItemGreen"
				if(b) {
					cl = "exItemRed"
				}	
				td(classes = cl) {
					a("#editByAuditoruum_" + props.num.toString()) {
						+"+"
						attrs.onClickFunction = { e->
							onClick(day) 
						}
					}
				}
            }
        }
		if(state.showSubDialog) {
			div("modalDialogSessionSubject") {
				attrs["id"] = "editByAuditoruum_" + props.num.toString()
				div {
					a("#", classes = "close") { +"X" }
					h2 { +"Назначение сессионных предметов для ауд. ${props.jsonAuditorium.name}" }
					
					if(insSelectDay != null) {
						val pojo = props.pojoSessionSubjects.get(insSelectDay) ?: PojoSessionSubject()
						table { tbody {
							tr {
								td { +"Группа"}
								td { +pojo.groupName}
								td {
									attrs["rowSpan"] = "4"
									button {
										+"Удалить" // 
										attrs.onClickFunction= { e->
											e.preventDefault()
											val self = state.sessionSubject?._links?.self ?: Href()
											val obj = UpdateSessionSubject(self)
											props.onDelete(obj)
										}
									}
								}
							}
							tr {
								td { +"Предмет " }
								td { +pojo.subjectName }
							}
							tr {
								td { +"Лектор" }
								td { +pojo.lectorName}
							}
							tr {
								td { +"Дата" }
								td { +(pojo.date.toString()+" января") }
							}
						}}
					}
					
					
					if(insSelectDay == null) {
						table { tbody {
							val groupsList = state.studentGroupsList
							tr {
								td { +"Группа"}
								td { 
									select(classes = "selectClasses") {
										attrs["ref"] = "selectGroup"
										
										option {
											+"(пусто)"
										}
										
										props.studentGroups.map { prop ->
											var show = true
											for(it in props.allSessionSubjects) {
												if(it.date == state.selectedDay) {
													val pojo = props.pojoSessionSubjects.get(it) ?: PojoSessionSubject()
													if(pojo.groupName == prop.name) {
														show = false
														break
													}
												}
											}
											if(show) {
												groupsList.add(prop)
											}
											
										}
										groupsList.map { prop ->
											option {
												+prop.name
											}
										}
										attrs.onChangeFunction = {
											val select = findDOMNode(refs["selectGroup"]) as HTMLSelectElement												
											val id = select.selectedIndex
											(findDOMNode(refs["selectSubject"]) as HTMLSelectElement).selectedIndex=0
											state.selectedSubjectBySessionSubjectNoDay = null
											if(id == 0) {
												state.selectedGroup = null
											} else {
												state.selectedGroup = state.studentGroupsList[id-1]
											}
											setState {}
										}
									}
								}
								td {
									attrs["rowSpan"] = "4"
									button {
										+"Создать"
										attrs.onClickFunction= { e->
											e.preventDefault()
											var sss = state.selectedSubjectBySessionSubjectNoDay
											if(sss != null) {
												Loader().getUpdateSessionSubject(sss) { item ->
													item.date = state.selectedDay
													item.auditorium = props.jsonAuditorium
													props.onUpdate(item)
												}
											}
										}
									}
								}
									
							}
							tr {
								td { +"Предмет " }
								td {
									select(classes = "selectClasses") {
										attrs["ref"] = "selectSubject"
										option {
											+"(пусто)"
										}
										val sel = state.selectedGroup
										if(sel != null) {
											val sessionSubjectsNoDayByGroup = state.sessionSubjectsNoDayByGroup
											for(it in state.sessionSubjectsNoDay) {
												val pojo = props.pojoSessionSubjects.get(it) ?: PojoSessionSubject()

												if(pojo.groupName == sel.name) {
													sessionSubjectsNoDayByGroup.add(it)
												}
											}
												
											state.sessionSubjectsNoDayByGroup.map { prop ->
												val pojo = props.pojoSessionSubjects.get(prop) ?: PojoSessionSubject()
												option {
													+pojo.subjectName
												}
											}
											attrs.onChangeFunction = {
												val select = findDOMNode(refs["selectSubject"]) as HTMLSelectElement												
												val id = select.selectedIndex
												if(id == 0) {
													state.selectedSubjectBySessionSubjectNoDay = null
												} else {
													state.selectedSubjectBySessionSubjectNoDay = state.sessionSubjectsNoDayByGroup[id-1]
												}
												setState {}
											}
										
										}
									}
								}
							}
							
							tr {
								td { +"Лектор" }
								td {
									val sel = state.selectedSubjectBySessionSubjectNoDay
									if(sel != null) {
										var pojo = props.pojoSessionSubjects.get(sel) ?: PojoSessionSubject()
										+pojo.lectorName
									} else {
										+"(пусто)"
									}								
								}
							}
							tr {
								td { +"Дата" }
								td { +(state.selectedDay.toString()+" января") }
							}
						} }
					}
					br{ }
					br{ }
				}
			}
		}
	}
}

fun RBuilder.ExamenByAuditoriumEdit(jsonAuditorium: JsonAuditorium,
							   num: Int,
							   sessionSubjects: List<JsonSessionSubject>,
							   pojoSessionSubjects: Map<JsonSessionSubject, PojoSessionSubject>,
							   studentGroups: List<JsonStudentGroup>,
							   allSessionSubjects: List<JsonSessionSubject>,
							   onUpdate: (UpdateSessionSubject)->Unit,
							   onDelete: (UpdateSessionSubject)->Unit)
							   = child(ExamenByAuditoriumEdit::class){
    attrs.jsonAuditorium = jsonAuditorium
    attrs.num = num
    attrs.sessionSubjects = sessionSubjects
    attrs.pojoSessionSubjects = pojoSessionSubjects
    attrs.studentGroups = studentGroups
    attrs.allSessionSubjects = allSessionSubjects
    attrs.onUpdate = onUpdate
    attrs.onDelete = onDelete
}

