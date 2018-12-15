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
import kotlin.js.Date

class ExamenByGroupEdit: RComponent<ExamenByGroupEdit.Props, ExamenByGroupEdit.State>(){
    var refs:dynamic = null
	
	interface Props: RProps {
        var jsonStudentGroup: JsonStudentGroup
		// sessionSubjects.groupName == jsonStudentGroup.groupName
		var sessionSubjects: List<JsonSessionSubject>
		var pojoSessionSubjects: Map<JsonSessionSubject,PojoSessionSubject>
		var auditoriums: List<JsonAuditorium>
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
		var selectedSubject: JsonSessionSubject?
		var auditoriumList: MutableList<JsonAuditorium>
    }
	
	init {
		state.showSubDialog = false
		state.selectedDay = 0
		state.selectedSubject = null
	}

	fun onClick(day: Int) {
		setState {
			selectedDay = day
			showSubDialog = true
		}
		//window.location.href = 
	}
	
    override fun RBuilder.render() {
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
            td { +props.jsonStudentGroup.name }
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
						a("#editByGroup_" + props.num.toString()) {
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
				attrs["id"] = "editByGroup_" + props.num.toString()
				div {
					a("#", classes = "close") { +"X" }
					h2 { +"Назначение сессионных предметов для группы ${props.jsonStudentGroup.name}" }
					
					if(insSelectDay != null) {
						val pojo = props.pojoSessionSubjects.get(insSelectDay) ?: PojoSessionSubject()
						table { tbody {
							tr {
								td { +"Предмет " }
								td { +pojo.subjectName }
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
								td { +"Лектор" }
								td { +pojo.lectorName}
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
								td { +"Предмет " }
								td {
									select(classes = "selectClasses") {
										attrs["ref"] = "selectSubject"
										option {
											+"(пусто)"
										}
										state.sessionSubjectsNoDay.map { prop ->
											var pojo = props.pojoSessionSubjects.get(prop) ?: PojoSessionSubject()
											option {
												+pojo.subjectName
											}
										}
										attrs.onChangeFunction = {
											val select = findDOMNode(refs["selectSubject"]) as HTMLSelectElement												
											val id = select.selectedIndex
											if(id == 0) {
												state.selectedSubject = null
											} else {
												state.selectedSubject = state.sessionSubjectsNoDay[id-1]
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
											//нужно назначить аудиторию и день
											val selectSubject = findDOMNode(refs["selectSubject"]) as HTMLSelectElement
											val idSubj = selectSubject.selectedIndex
											
											val selectAud = findDOMNode(refs["selectAuditorium"]) as HTMLSelectElement
											val idAud = selectAud.selectedIndex
											
											if(idSubj!=0 && idAud!=0) {
												var subj = state.sessionSubjectsNoDay[idSubj-1]
												Loader().getUpdateSessionSubject(subj) { item ->
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
								td { +"Лектор" }
								td {
									val sel = state.selectedSubject
									if(sel != null) {
										var pojo = props.pojoSessionSubjects.get(sel) ?: PojoSessionSubject()
										+pojo.lectorName
									} else {
										+"(пусто)"
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


fun RBuilder.ExamenByGroupEdit(studentGroup: JsonStudentGroup,
							   num: Int,
							   sessionSubjects: List<JsonSessionSubject>,
							   pojoSessionSubjects: Map<JsonSessionSubject, PojoSessionSubject>,
							   auditoriums: List<JsonAuditorium>,
							   allSessionSubjects: List<JsonSessionSubject>,
							   onUpdate: (UpdateSessionSubject)->Unit,
							   onDelete: (UpdateSessionSubject)->Unit)
							   = child(ExamenByGroupEdit::class){
    attrs.jsonStudentGroup = studentGroup
    attrs.num = num
    attrs.sessionSubjects = sessionSubjects
    attrs.pojoSessionSubjects = pojoSessionSubjects
    attrs.auditoriums = auditoriums
    attrs.allSessionSubjects = allSessionSubjects
    attrs.onUpdate = onUpdate
    attrs.onDelete = onDelete
}
