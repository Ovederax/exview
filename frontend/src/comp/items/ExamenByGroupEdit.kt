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

class ExamenByGroupEdit: RComponent<ExamenByGroupEdit.Props, ExamenByGroupEdit.State>(){
    var refs:dynamic = null
	
	interface Props: RProps {
        var jsonStudentGroup: JsonStudentGroup
		// sessionSubjects.groupName == jsonStudentGroup.groupName
		var sessionSubjects: List<JsonSessionSubject>
		var pojoSessionSubjects: Map<JsonSessionSubject,PojoSessionSubject>
        var num: Int
    }

    interface State: RState {
        var showSubDialog: Boolean
		var sessionSubjectsNoDay: MutableList<JsonSessionSubject>
		var selectedDay: Int
		var sessionSubject: JsonSessionSubject?
		var selectedSubject: JsonSessionSubject?
    }
	
	init {
		state.showSubDialog = false
		state.selectedDay = 0
		state.selectedSubject = null
	}

    fun reloadData() {
        /*Loader().getSessionSubjectByGroup(props.jsonStudentGroup)
        { it ->
            props.sessionSubjects = it
            var counter = 0
            val size = props.sessionSubjects.size-1
            for(i in 0..size) {
                Loader().getPojoSessionSubject(props.sessionSubjects[i]) {
                    props.pojoSessionSubjects.put(props.sessionSubjects[i],it)
                    counter += 1
                    if(counter == props.sessionSubjects.size) {
                        setState {}
                    }
                }
            }
            setState{}
        }*/
    }

    override fun componentDidMount() {
        reloadData()
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
            for (i in 0..30) {
                td {
					val day = i+1
					//val id = props.num*100+i
					a("#editByGroup_" + props.num.toString()) {
						if(insSelectDay == null) {
							+"+"
						}else {
							+"o"
						}
						attrs.onClickFunction = { onClick(day) }
					}
                }
            }
		}
		
		if(state.showSubDialog) {
			div("modalDialog") {
				attrs["id"] = "editByGroup_" + props.num.toString()
				div {
					a("#", classes = "close") { +"X" }
					h2 { +"Назначение сессионных предметов для группы ${props.jsonStudentGroup.name}" }
					
					if(insSelectDay != null) {
						var pojo = props.pojoSessionSubjects.get(insSelectDay) ?: PojoSessionSubject()
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

											/*if(idSubj!=0 && idLect!=0) {
												var lectorList = props.lectorsMap.get(state.subjectsToSelect[idSubj-1])
												if(lectorList != null) {
													var pair = PairGroupSubjectLector()
													pair.group = props.group
													pair.subject = state.subjectsToSelect[idSubj-1]
													pair.lector = lectorList[idLect-1]
													Loader().createSessionSubject(pair) {
														selectSubject.selectedIndex = 0
														selectLector.selectedIndex = 0
														reload()
													}
												}*/
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
										+"Создать" // Удалить
										attrs.onClickFunction= { e->
											e.preventDefault()
											var selectSubject = findDOMNode(refs["selectSubject"]) as HTMLSelectElement
											val idSubj = selectSubject.selectedIndex
											var selectLector = findDOMNode(refs["selectLector"]) as HTMLSelectElement
											val idLect = selectLector.selectedIndex

											if(idSubj!=0 && idLect!=0) {
												/*var lectorList = props.lectorsMap.get(state.subjectsToSelect[idSubj-1])
												if(lectorList != null) {
													var pair = PairGroupSubjectLector()
													pair.group = props.group
													pair.subject = state.subjectsToSelect[idSubj-1]
													pair.lector = lectorList[idLect-1]
													Loader().createSessionSubject(pair) {
														selectSubject.selectedIndex = 0
														selectLector.selectedIndex = 0
														reload()
													}
												}*/
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
							tr {
								td { +"Аудитория"}
								td { 
									select(classes = "selectClasses") {
									attrs["ref"] = "selectAuditorium"
									
									option {
										+"(пусто)"
									}
									/*val sel = state.selectedSubject
									if(sel != null) {
										val lectors = props.lectorsMap.get(sel)
										if(lectors != null) {
											lectors.map { prop ->
												option {
													+prop.name
												}
											}
										}
									}*/
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
//                //props.jsonLector._links.subjects?.href,
//                UpdateDialogSubjects(props.jsonLector,props.num, props.allSubjects) { it ->
//                    //добавление предметов лектору
//                    val pair = PairLectorSubjects()
//                    pair.lector = props.jsonLector
//                    pair.subjects = it
//                    Loader().setSubjectsInLector(pair) {
//                        reloadData()





fun RBuilder.ExamenByGroupEdit(studentGroup: JsonStudentGroup, num: Int, sessionSubjects: List<JsonSessionSubject>,
	pojoSessionSubjects: Map<JsonSessionSubject, PojoSessionSubject>) = child(ExamenByGroupEdit::class) {
    attrs.jsonStudentGroup = studentGroup
    attrs.num = num
    attrs.sessionSubjects = sessionSubjects
    attrs.pojoSessionSubjects = pojoSessionSubjects
}
