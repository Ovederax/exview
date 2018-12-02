package comp

import kotlinx.html.js.onClickFunction
import kotlinx.html.*
import org.w3c.dom.*
import react.*
import react.dom.*
import rest.*
import kotlin.browser.window
import org.w3c.dom.events.Event

class Examen: RComponent<Examen.Props, Examen.State>() {
    var refs:dynamic = null
    interface Props:RProps {
		var attributes: List<Property>
    }
	
	interface State: RState {
		var type: TypeShow
	}
	
	enum class TypeShow {
        LECTOR, STUDENT_GROUP, SUBJECT
    }
	
	init {
		state.type = TypeShow.STUDENT_GROUP
	}
	
	override fun componentDidMount() {
        loadInfo(state.type)
    }
	
	private fun loadInfo(type: TypeShow) {
		state.type = type
		setState {}
	}
    
	override fun RBuilder.render() {
        div() {
			+"Просмотр календаря экзаменов"
			div() {
				button (classes = "materialBtn") { +"По преподователю";   	attrs.onClickFunction = { loadInfo(TypeShow.LECTOR) } }
				button (classes = "materialBtn") { +"По группе";    	 	attrs.onClickFunction = { loadInfo(TypeShow.STUDENT_GROUP) } }
				button (classes = "materialBtn") { +"По предмету";   		attrs.onClickFunction = { loadInfo(TypeShow.SUBJECT) } }
			}
			p() {	
				table {
					tbody {
						tr {
							when(state.type) {
								TypeShow.LECTOR -> th { +"Преподователь" }
								TypeShow.STUDENT_GROUP -> th { +"Группа" }
								TypeShow.SUBJECT -> th { +"Предмет" }
								else -> {}
							}
							
							for(i in 0..30) {
								th { +(i+1).toString() }
							}
						}
					}
				}
			}
		}
	}
}
			
//            //attrs.key = props.employee.entity._links.self.href
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
//                                attrs["key"] = prop.title
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







