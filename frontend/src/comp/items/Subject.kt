package comp.items

import kotlinx.html.js.onClickFunction
import model.*
import react.*
import react.dom.*
import rest.*

class Subject: RComponent<Subject.Props, Subject.State>(){
	interface Props: RProps {
        var jsonSubject: JsonSubject
        var key:String
        var onDelete:()->Unit
		var num: Int
    }
    
	interface State: RState {
		var lectors: List<JsonLector>
		//var studentGroups: List<JsonStudentGroup>
	}
	
	init {
		//state.subjects = ArrayList()
		state.lectors = ArrayList()
	}
	
	override fun componentDidMount() {
		Loader().getLectorsBySubject(props.jsonSubject) { lectors ->
			state.lectors = lectors
			setState{}
		}
	}
	
	override fun RBuilder.render() {
        tr {
            td { +props.num.toString() }
            td { +props.jsonSubject.name }
			td {
				div {
					a("#showLectors_"+props.num.toString()) {
						+"Просмотр"
					}
				}
				div("modalDialog") {
					attrs["id"] = "showLectors_"+props.num.toString()
					div {
						p{ strong {
							+"Лекторы, ведущие предмет \"${props.jsonSubject.name}\""
						}}
						a("#", classes = "close") { 
							+"X"
						}
						table {
							for(i in 0..state.lectors.size-1) {
								tr {
									+state.lectors[i].name
								}
							}
						}
					}
				}	 
            }
            td {
                button {
                    attrs { onClickFunction = {
                            +props.jsonSubject._links.studentGroup!!.href
                        }}
                    +"Просмотр"
                }
            }
			
			
			td {
                button {
                    attrs { onClickFunction = {
                        props.onDelete()
                    } }
                    +"Удалить"
                }
            }
        }
    }
}
fun RBuilder.Subject(jsonSubject: JsonSubject, num: Int, onDelete:()->Unit = {})
        = child(Subject::class) {
    attrs.jsonSubject = jsonSubject
    attrs.onDelete = onDelete
    attrs.num = num
}
