package comp.items

import model.*
import react.*
import react.dom.*
import kotlinx.html.js.*
import rest.*
import org.w3c.dom.*
import kotlin.js.Date

class ExamenByLectorEdit: RComponent<ExamenByLectorEdit.Props, ExamenByLectorEdit.State>(){
    var refs:dynamic = null
	
	interface Props: RProps {
		var num: Int
        var lector: JsonLector
		var sessionSubjects: List<JsonSessionSubject>
		var allSessionSubjects: List<JsonSessionSubject>
		var pojoSessionSubjects: Map<JsonSessionSubject,PojoSessionSubject>
        
		var studentGroups: List<JsonStudentGroup>
        var auditoriums: List<JsonAuditorium>		
		
		var onUpdate: (UpdateSessionSubject)->Unit
		var onDelete: (UpdateSessionSubject)->Unit
    }

    interface State: RState {
        var showSubDialog: Boolean
		var sessionSubjectsNoDay: MutableList<JsonSessionSubject>
		
		var selectedDay: Int	
		var sessionSubject: JsonSessionSubject?	
		
		var auditoriumList: MutableList<JsonAuditorium>
        var studentGroupsList: MutableList<JsonStudentGroup>
        var selectedGroup: JsonStudentGroup?
        var sessionSubjectsNoDayByGroup: MutableList<JsonSessionSubject>
        var selectedSubjectBySessionSubjectNoDay: JsonSessionSubject?
    }
	
	init {
		state.showSubDialog = false
		state.selectedDay = 0
	}

	private fun onClick(day: Int) {
		setState {
			selectedDay = day
			showSubDialog = true
		}
	}
	
    override fun RBuilder.render() {
        state.sessionSubjectsNoDayByGroup = ArrayList()
        state.studentGroupsList = ArrayList()
		state.sessionSubjectsNoDay = ArrayList()
		state.auditoriumList = ArrayList()
		state.sessionSubject = null
		
		for(it in props.sessionSubjects) {
			if(it.date == -1) {
				state.sessionSubjectsNoDay.add(it)
			} else if(it.date == state.selectedDay) {
				state.sessionSubject = it
			}
		}
		
		val insSelectDay = state.sessionSubject
		tr {
            td { +props.lector.name }
            for (i in 6..26) {
				var b = false
				val day = i+1
				val date = Date(2019,0,day)
				for(it in props.sessionSubjects) {
					if(it.date == day) {
						b = true
					}
				}
				if(date.getDay() == 0) {
					td(classes = "exItemGree") {
						+" "
					}
				}else {
					var cl = "exItemGreen"
					if (b) {
						cl = "exItemRed"
					}
					td(classes = cl) {
						a("#editByLector_" + props.num.toString()) {
							+"+"
							attrs.onClickFunction = {
								onClick(day)
							}
						}
					}
				}
            }
		}
		
		if(state.showSubDialog) {
			div("modalDialogSessionSubject") {
				attrs["id"] = "editByLector_" + props.num.toString()
				div {
					a("#", classes = "close") { +"X" }
					h2 { +"Назначение сессионных предметов" }
					
					if(insSelectDay != null) {
						val pojo = props.pojoSessionSubjects.get(insSelectDay) ?: PojoSessionSubject()
						table { tbody {
							tr {
								td { +"Преподователь"}
								td { +props.lector.name }							
								td {
									attrs["rowSpan"] = "5"
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
								td { +"Группа" }
								td { +pojo.groupName}
							}
							tr {
								td { +"Предмет " }
								td { +pojo.subjectName }
							}
							tr {
								td { +"Аудитория"}
								td { +pojo.auditorium}
							}
							tr {
								td { +"Дата" }
								td { +(pojo.date.toString()+" января") }
							}
						}}
					}

					if(insSelectDay == null) {
						table { tbody {
							tr {
								td { +"Преподователь"}
								td { +props.lector.name }							
								td {
									attrs["rowSpan"] = "5"
									button {
										+"Создать"
										attrs.onClickFunction= { e->
											e.preventDefault()

											val selectSubject = findDOMNode(refs["selectSubject"]) as HTMLSelectElement
											val subjectName = selectSubject.value

											val selectedGroup = state.selectedGroup
											
											
											val selectAud = findDOMNode(refs["selectAuditorium"]) as HTMLSelectElement
											val idAud = selectAud.selectedIndex
											
											if(idAud!=0 && selectedGroup != null) {
												var sessionSubject: JsonSessionSubject? = null
												for(item in props.sessionSubjects) {
													val pojo = props.pojoSessionSubjects[item]
													if(pojo != null) {
														if(pojo.groupName == selectedGroup.name && pojo.subjectName == subjectName) {
															sessionSubject = item
														}
													}

												}
												if(sessionSubject != null)
												Loader().getUpdateSessionSubject(sessionSubject) { item ->
													item.date = state.selectedDay
													item.auditorium = state.auditoriumList[idAud-1]
													props.onUpdate(item)
												}
											}
										}
									}
								}
							}
							tr {
								td { +"Группа"}
								td { 
									select(classes = "selectClasses") {
										attrs["ref"] = "selectGroup"
										option {
											+"(пусто)"
										}
										
										props.studentGroups.map { prop ->
											var item = prop
											var show = false
											var dp = true
											for(it in props.allSessionSubjects) {
												val pojo = props.pojoSessionSubjects[it] ?: PojoSessionSubject()
												if(pojo.groupName == prop.name && pojo.lectorName == props.lector.name) {
													show = true
													if(pojo.date == state.selectedDay) {
														dp = false
														break
													}
												}
											}
											if(show && dp) {
												state.studentGroupsList.add(prop)
											}
											
										}
										state.studentGroupsList.map { prop ->
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
							}
							tr {
								td { +"Предмет " }
								td {
									select(classes = "selectClasses") {
										attrs["ref"] = "selectSubject"
										option {
											+"(пусто)"
										}
										val selectedGroup = state.selectedGroup
										if(selectedGroup != null) {
											state.sessionSubjectsNoDay.map { prop ->
												val pojo = props.pojoSessionSubjects[prop] ?: PojoSessionSubject()
												if(pojo.groupName == selectedGroup.name) {
													option {
														+pojo.subjectName
													}
												}

											}
										}
									}
								}
							}
							val auditoriumList = state.auditoriumList
							tr {
								td { +"Аудитория"}
								td { 
									select(classes = "selectClasses") {
										attrs["ref"] = "selectAuditorium"
										
										option {
											+"(пусто)"
										}
										
										props.auditoriums.map { prop ->
											var show = true
											for(it in props.allSessionSubjects) {
												if(it.date == state.selectedDay) {
													val pojo = props.pojoSessionSubjects.get(it) ?: PojoSessionSubject()
													if(pojo.auditorium == prop.name) {
														show = false
														break
													}
												}
											}
											if(show) {
												auditoriumList.add(prop)
											}
											
										}
										auditoriumList.map { prop ->
											option {
												+prop.name
											}
										}
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


fun RBuilder.ExamenByLectorEdit(lector: JsonLector,
							   num: Int,
							   sessionSubjects: List<JsonSessionSubject>,
							   pojoSessionSubjects: Map<JsonSessionSubject, PojoSessionSubject>,
                               studentGroups: List<JsonStudentGroup>,
                               auditoriums: List<JsonAuditorium>,
							   allSessionSubjects: List<JsonSessionSubject>,
							   onUpdate: (UpdateSessionSubject)->Unit,
							   onDelete: (UpdateSessionSubject)->Unit)
							   = child(ExamenByLectorEdit::class){
    attrs.lector = lector
    attrs.num = num
    attrs.sessionSubjects = sessionSubjects
    attrs.auditoriums = auditoriums
    attrs.pojoSessionSubjects = pojoSessionSubjects
    attrs.studentGroups = studentGroups
    attrs.allSessionSubjects = allSessionSubjects
    attrs.onUpdate = onUpdate
    attrs.onDelete = onDelete
}
