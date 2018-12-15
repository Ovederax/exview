package comp

import comp.items.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import rest.*
import org.w3c.dom.events.Event
import model.*
import kotlin.browser.window
import kotlin.js.Date

class Examen: RComponent<Examen.Props, Examen.State>() {
    var refs:dynamic = null

	interface Props:RProps {
		
    }
	
	init {
	    state.auditoriums = ArrayList()
	    state.sessionSubjects = ArrayList()
	    state.studentGroups = ArrayList()
	    state.lectors = ArrayList()
        state.pojoSessionSubjects = HashMap()
	    state.type = TypeShow.STUDENT_GROUP
	}

	interface State: RState {
		var sessionSubjects: List<JsonSessionSubject>
		
		//[optimization]//
		var sessionSubjectsByGroup:      MutableMap<String, MutableList<JsonSessionSubject>>
		var sessionSubjectsByLectors:    MutableMap<String, MutableList<JsonSessionSubject>>
		var sessionSubjectsByAuditorium: MutableMap<String, MutableList<JsonSessionSubject>>

		var pojoSessionSubjects: MutableMap<JsonSessionSubject,PojoSessionSubject>
		var studentGroups: 	List<JsonStudentGroup>
		var auditoriums: 	List<JsonAuditorium>
		var lectors: 		List<JsonLector>
		var type: TypeShow
		var subjectsMap: MutableMap<JsonSessionSubject, JsonSubject>
	}
	enum class TypeShow {
        STUDENT_GROUP, AUDITORIUM, LECTORS
    }
	
	override fun componentDidMount() {
        loadInfo()
    }
	
	private fun loadInfo() {
		Loader().getAllSessionSubject {
			state.sessionSubjects = it
			Loader().loadAuditoriumList {
				state.auditoriums = it
				Loader().loadGroupList {
					state.studentGroups = it
					Loader().loadLectorsList {
						state.lectors = it
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
	}
    fun onDelete(pojo: UpdateSessionSubject) {
		Loader().freeSessionSubject(pojo) {
			loadInfo()
		}
	}
	fun onUpdate(pojo: UpdateSessionSubject){
		Loader().refreshSessionSubject(pojo) {
			loadInfo()
		}
	}
	override fun RBuilder.render() {
		state.sessionSubjectsByGroup		= HashMap()
		state.sessionSubjectsByAuditorium	= HashMap()
		state.sessionSubjectsByLectors	    = HashMap()

		for(item in state.sessionSubjects) {
			val pojo = state.pojoSessionSubjects.get(item)
			if(pojo == null) {
				continue
			}
			val groupName     : String = pojo.groupName
			val auditoriumName: String = pojo.auditorium
			val lectorName:     String = pojo.lectorName

			var listByGroup = state.sessionSubjectsByGroup.get(groupName)
			if(listByGroup == null) {
				listByGroup = ArrayList()
				state.sessionSubjectsByGroup.put(groupName,listByGroup)
			}
			listByGroup.add(item)

            var listByLector = state.sessionSubjectsByLectors.get(lectorName)
            if(listByLector == null) {
                listByLector = ArrayList()
                state.sessionSubjectsByLectors.put(lectorName,listByLector)
            }
            listByLector.add(item)

			if(auditoriumName != "") {
				var listByAud = state.sessionSubjectsByAuditorium.get(auditoriumName)
				if(listByAud == null) {
					listByAud = ArrayList()
					state.sessionSubjectsByAuditorium.put(auditoriumName,listByAud)
				}
				listByAud.add(item)
			}
		}

        div() {
			h2 { +"Просмотр календаря экзаменов" }
			p {
                button (classes = "materialBtn") { +"По группам";    	 attrs.onClickFunction = { setState { type = TypeShow.STUDENT_GROUP } } }
				button (classes = "materialBtn") { +"По аудиториям"; 	 attrs.onClickFunction = { setState { type = TypeShow.AUDITORIUM    } } }
				button (classes = "materialBtn") { +"По преподователям"; attrs.onClickFunction = { setState { type = TypeShow.LECTORS    } } }
				button (classes = "materialBtn") { 
				+"Экспорт";     	 
				attrs.onClickFunction = {
					var str = ""
					when(state.type) {
						TypeShow.AUDITORIUM -> str = "http://localhost:8080/download/table_by_auditorium"
						TypeShow.STUDENT_GROUP -> str = "http://localhost:8080/download/timetable"
						TypeShow.LECTORS -> str = "http://localhost:8080/download/table_by_lector"
					}
					window.location.href = str
				}
			}}
            table {
                tbody {
                    tr {
                        when(state.type) {
                            TypeShow.AUDITORIUM -> th { +"Аудитория" }
                            TypeShow.STUDENT_GROUP -> th { +"Группа" }
                            TypeShow.LECTORS -> th { +"Преподователь" }
                            else -> {
                            }
                        }
                        for(i in 6..26) {
                            val day = i+1
							val date = Date(2019,0,day)
							if(date.getDay() == 0) {
								th(classes = "exItemGree") {
									+(i+1).toString()
								}
							}else {
								th {
									+(i+1).toString() 
								}
							}
                        }
                    }
                    when(state.type) {
                        TypeShow.AUDITORIUM -> {
                            var count = 1
                            for(it in state.auditoriums) {
								val list = state.sessionSubjectsByAuditorium.get(it.name) ?: ArrayList()
                                ExamenByAuditoriumEdit(it, count, list, state.pojoSessionSubjects, state.studentGroups, state.sessionSubjects, ::onUpdate, ::onDelete)
                                ++count
                            }
                        }
                        TypeShow.STUDENT_GROUP -> {
                            var count = 1
                            for(it in state.studentGroups) {
								val list = state.sessionSubjectsByGroup.get(it.name)
								if(list != null) {
									ExamenByGroupEdit(it, count, list, state.pojoSessionSubjects, state.auditoriums, state.sessionSubjects, ::onUpdate, ::onDelete)
									++count
								}
                            }
                        }
						TypeShow.LECTORS -> {
                            var count = 1
                            for(it in state.lectors) {
								val list = state.sessionSubjectsByLectors.get(it.name)
								if(list != null) {
									ExamenByLectorEdit(it, count, list, state.pojoSessionSubjects, state.studentGroups, state.auditoriums, state.sessionSubjects, ::onUpdate, ::onDelete)
									++count
								}
                            }
                        }
                    }
                }
            }

		}
	}
}

fun RBuilder.Examen() = child(Examen::class) {

}







