package comp

import comp.items.ExamenByAuditoriumEdit
import comp.items.ExamenByGroupEdit
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import rest.*
import org.w3c.dom.events.Event
import model.*

class Examen: RComponent<Examen.Props, Examen.State>() {
    var refs:dynamic = null

	interface Props:RProps {
		
    }
	
	init {
	    state.auditoriums = ArrayList()
	    state.sessionSubjects = ArrayList()
	    state.studentGroups = ArrayList()
        state.pojoSessionSubjects = HashMap()
	    state.type = TypeShow.STUDENT_GROUP
	}

	interface State: RState {
		var sessionSubjects: List<JsonSessionSubject>
		
		//[optimization]//
		var sessionSubjectsByGroup: MutableMap<String, MutableList<JsonSessionSubject>>
		var sessionSubjectsByAuditorium: MutableMap<String, MutableList<JsonSessionSubject>>
		var sessionSubjectsNoAuditorium: MutableList<JsonSessionSubject>
       
		var pojoSessionSubjects: MutableMap<JsonSessionSubject,PojoSessionSubject>
		var studentGroups: List<JsonStudentGroup>
		var auditoriums: List<JsonAuditorium>
		var type: TypeShow
	}
	enum class TypeShow {
        STUDENT_GROUP, AUDITORIUM
    }
	
	override fun componentDidMount() {
        loadInfo()
    }
	
	private fun loadInfo() {
		/*Loader().loadAllSubjects() {
			state.subjects = it
			var counter = 0
			var size = state.subjects.size-1
			for(i in 0..size) {
				Loader().getLectorsBySubject(state.subjects[i]) {
					state.lectorsMap.put(state.subjects[i], it)
					counter += 1
					if(counter == state.subjects.size) {
						setState {}
						call()
					}
				}
			}
		}*/
		Loader().getAllSessionSubject {
			state.sessionSubjects = it
			Loader().loadAuditoriumList {
				state.auditoriums = it
				Loader().loadGroupList {
					state.studentGroups = it
					
					state.pojoSessionSubjects = HashMap()
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
				}
			}
		}
	}
    
	override fun RBuilder.render() {
		state.sessionSubjectsByGroup		= HashMap()
		state.sessionSubjectsByAuditorium	= HashMap()
		state.sessionSubjectsNoAuditorium	= ArrayList()
		for(item in state.sessionSubjects) {
			val pojo = state.pojoSessionSubjects.get(item)
			if(pojo == null) {
				continue
			}
			val groupName     : String = pojo.groupName
			val auditoriumName: String = pojo.auditorium
			var listByGroup = state.sessionSubjectsByGroup.get(groupName)
			if(listByGroup == null) {
				listByGroup = ArrayList()
				state.sessionSubjectsByGroup.put(groupName,listByGroup)
			}
			listByGroup.add(item)
			
			if(auditoriumName == "") {
				state.sessionSubjectsNoAuditorium.add(item)
			}else {
			
				var listByAud = state.sessionSubjectsByAuditorium.get(auditoriumName)
				if(listByAud == null) {
					listByAud = ArrayList()
					state.sessionSubjectsByAuditorium.put(auditoriumName,listByAud)
				}
				listByAud.add(item)
			}
		}
        div() {
			h4 { +"Просмотр календаря экзаменов" }
			div() {
                button (classes = "materialBtn") { +"По группе";    	attrs.onClickFunction = { setState { type = TypeShow.STUDENT_GROUP } } }
				button (classes = "materialBtn") { +"По аудитории";   	attrs.onClickFunction = { setState { type = TypeShow.AUDITORIUM    } } }
			}
			br { }
            table {
                tbody {
                    tr {
                        when(state.type) {
                            TypeShow.AUDITORIUM -> th { +"Аудитория" }
                            TypeShow.STUDENT_GROUP -> th { +"Группа" }
                            else -> {
                            }
                        }
                        for(i in 0..30) {
                            th { +(i+1).toString() }
                        }
                    }
                    when(state.type) {
                        TypeShow.AUDITORIUM -> {
                            var count = 1
                            for(it in state.auditoriums) {
								
                                ExamenByAuditoriumEdit(it, count, state.sessionSubjects, state.pojoSessionSubjects)
                                ++count
                            }
                        }
                        TypeShow.STUDENT_GROUP -> {
                            var count = 1
                            for(it in state.studentGroups) {
                                //console.log(it.name)
								val list = state.sessionSubjectsByGroup.get(it.name) ?: ArrayList()
								//val list = state.sessionSubjects
								/*for(it in list) {
                                    val pojo = state.pojoSessionSubjects.get(it)
                                    if(pojo != null) {
                                        console.log(pojo.groupName)
                                        console.log(pojo.subjectName)
                                        console.log(pojo.lectorName) 
                                    }
									console.log("")
                                }*/
								
                                ExamenByGroupEdit(it, count, list, state.pojoSessionSubjects)
                                ++count
                            }
                        }
                    }
                }
            }

		}
	}
}
			
//            //attrs.num = props.employee.entity._links.self.href
//            //var dialogId = "updateEmployee-${props.employee.entity._links.self.href}"
//            val str = props.employee._links.self?.href?.substringAfter("http://localhost:8080/")?.replace("/","-")
//            val dialogId = "updateEmployee-$str"
//            a("#$dialogId") { +"Update" }
//            div("modalDialog") {
//                attrs["id"] = dialogId
//                div {
//                    a("#", classes = "close") { +"X" }
//                    h2 { +"Update an employee" }
//
//                    var i =0;
//                    val arrEmp = toPropsArray(props.employee)
//                    form {
//                        props.attributes.map { prop ->
//                            p() {
//                                attrs["num"] = prop.title
//                                input(type = InputType.text, classes = "field") {
//                                    attrs["placeholder"] = prop.title
//                                    attrs["ref"] = prop.title
//                                    attrs.defaultValue = arrEmp[i]
//                                    ++i
//                                }
//                            }
//                        }
//                        button {
//                            attrs { onClickFunction = { handleSubmit(it) } }
//                            +"Update"
//                        }
//                    }
//                }
//            }


    fun handleSubmit(e: Event) {
//        e.preventDefault()
//        val updatedEmployee = Array(props.attributes.size) {
//            (findDOMNode(this.refs[props.attributes[it].title]) as HTMLInputElement).value.trim()
//        }
//        props.onUpdate(fromPropsArray(updatedEmployee,props.employee._links))
//        for(attr in props.attributes){
//            (findDOMNode(this.refs[attr.title]) as HTMLInputElement).value = ""
//        }
//        window.location.href = "#"
    }


fun RBuilder.Examen() = child(Examen::class) {

}







