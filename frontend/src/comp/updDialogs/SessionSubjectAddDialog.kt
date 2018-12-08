package comp.updDialogs

import kotlinx.html.js.*
import model.*
import org.w3c.dom.*
import org.w3c.dom.events.Event
import react.*
import rest.*
import react.dom.*
import kotlin.browser.*

class SessionSubjectAddDialog : RComponent<SessionSubjectAddDialog.Props, SessionSubjectAddDialog.State>() {
    var refs:dynamic = null

    interface Props : RProps {
        var group: JsonStudentGroup
        var num: Int
		var subjects: List<JsonSubject>
        var lectorsMap: MutableMap<JsonSubject, List<JsonLector>>
    }
	
	init {
		state.subjectsToSelect = ArrayList()
		state.selectedSubject = null
		state.sessionSubjects = ArrayList()
		state.pojoSessionSubjects = HashMap()
	}
	
	override fun componentDidMount() {
		reload()
	}
	
	fun reload() {
		Loader().getSessionSubjectByGroup(props.group) {
			state.sessionSubjects = it
			state.pojoSessionSubjects = HashMap()
			//setState {}
			var counter = 0
			val size = state.sessionSubjects.size-1
			for(i in 0..size) {
				Loader().getPojoSessionSubject(state.sessionSubjects[i]) {
					state.pojoSessionSubjects.put(state.sessionSubjects[i],it)
					counter += 1
					if(counter == state.sessionSubjects.size) {
						setState {}
					}
				}
			}
			if(size == -1) {
				setState {}
			}
		}
	}
	
	interface State: RState {
		var selectedSubject: JsonSubject?
		var sessionSubjects: List<JsonSessionSubject> 
		var subjectsToSelect: MutableList<JsonSubject> 
		var pojoSessionSubjects: MutableMap<JsonSessionSubject,PojoSessionSubject> 
	}

    fun handleSubmit(e: Event) {
        e.preventDefault()
        window.location.href = "#"
    }

    override fun RBuilder.render() {
		state.subjectsToSelect = ArrayList()
		props.subjects.map { prop ->
			var ch = true
			for (it in state.sessionSubjects) {
				var item = state.pojoSessionSubjects.get(it)
				if(item != null)
				if(item.subjectName == prop.name) {
					ch = false
				}
			}
			if(ch) {
				state.subjectsToSelect.add(prop)
			}
		}
		
		
        div {
            a("#setSubj_" + props.num.toString()) {
                +"Просмотр"
            }
            div("modalDialogSessionSubject") {
				attrs["styles"] = "{width: 800px}"
                attrs["id"] = "setSubj_" + props.num.toString()
                div {
                    a("#", classes = "close") { +"X" }
                    h2 { +"Создание сессионных предметов группы ${props.group.name}" }
					table { tbody {
						attrs["border"] = "0px"
						tr {
							td {
								+"Предмет "
							}
							td {
								select(classes = "selectClasses") {
									attrs["ref"] = "selectSubject"
									attrs.onChangeFunction = {
										val select = findDOMNode(refs["selectSubject"]) as HTMLSelectElement
										val id = select.selectedIndex
										if(id == 0) {
											state.selectedSubject = null
										} else {
											state.selectedSubject = state.subjectsToSelect[id-1]
										}
										(findDOMNode(refs["selectLector"]) as HTMLSelectElement).selectedIndex = 0 
										setState {}
									}
									option {
										+"(пусто)"
									}
									state.subjectsToSelect.map { prop ->
										var ch = true
										for (it in state.sessionSubjects) {
											var item = state.pojoSessionSubjects.get(it)
											if(item != null)
											if(item.subjectName == prop.name) {
												ch = false
											}
										}
										if(ch)
										option {
											+prop.name
										}
									}
								}
							}
							td {
								attrs["rowSpan"] = "2"
								button {
									+"Создать"
									attrs.onClickFunction= { e->
										e.preventDefault()
										var selectSubject = findDOMNode(refs["selectSubject"]) as HTMLSelectElement
										val idSubj = selectSubject.selectedIndex
										var selectLector = findDOMNode(refs["selectLector"]) as HTMLSelectElement
										val idLect = selectLector.selectedIndex
										
										if(idSubj!=0 && idLect!=0) {
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
											}
										}
									}
								}
							}
						}
						tr {
							td { +"Лектор" }
							td {
								select(classes = "selectClasses") {
									attrs["ref"] = "selectLector"
									option {
										+"(пусто)"
									}
									val sel = state.selectedSubject
									if(sel != null) {
										val lectors = props.lectorsMap.get(sel)
										if(lectors != null) {
											lectors.map { prop ->
												option {
													+prop.name
												}
											}
										}
									}
								}
							}
						}
					} }
					if(state.sessionSubjects.size>0) {
						br{ }
						br{ }
						table { tbody {
							tr {
								th { +"Предмет"}
								th { +"Преподователь"}
								th { +"Дата сдачи"}
								th { +"Аудитория"}
								th { +"Удалить"}
							}
							for (it in state.sessionSubjects) {
								var item = state.pojoSessionSubjects.get(it)
								if(item != null) {
									tr {
										td {+item.subjectName}
										td {+item.lectorName}
										if(item.date != -1) td {+(item.date.toString()+".1.18")} else td {+"-"}
										if(item.auditorium != "") td {+item.auditorium} else td {+"-"}
										td {button {
											attrs.onClickFunction = { e->
												var self = it?._links?.self ?: Href()
												var obj = UpdateSessionSubject(self)
												Loader().deleteSessionSubject(obj) {
													reload() 
												}
											}
											+"Удалить"
										}}
									}
								}
							}
						}}
						br{ }
						br{ }
					}

                }
            }
        }
    }
}


fun RBuilder.SessionSubjectAddDialog(group: JsonStudentGroup, num: Int, subjects: List<JsonSubject>, lectorsMap: MutableMap<JsonSubject, List<JsonLector>>)
        = child(SessionSubjectAddDialog::class) {
    attrs.group = group
    attrs.num = num
	attrs.subjects = subjects
    attrs.lectorsMap = lectorsMap
}