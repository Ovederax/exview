package comp

//import kotlinx.html.InputType
//import kotlinx.html.js.onClickFunction
//import org.w3c.dom.HTMLInputElement
//import org.w3c.dom.events.Event
//import react.RBuilder
//import react.RComponent
//import react.RProps
//import react.RState
//import react.dom.*
//import rest.JsonEmployee
//import rest.Property
//import rest.fromPropsArray
//import rest.toPropsArray
//import kotlin.browser.window
//
//class UpdateDialog: RComponent<UpdateDialog.Props, RState>() {
//    var refs:dynamic = null
//    interface Props:RProps{
//        var attributes: List<Property>
//        var onUpdate:(JsonEmployee)->Unit
//        var employee: JsonEmployee
//    }
//
//    override fun RBuilder.render() {
//        div() {
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
//        }
//    }
//
//
//    fun handleSubmit(e: Event) {
//        e.preventDefault()
//        val updatedEmployee = Array(props.attributes.size) {
//            (findDOMNode(this.refs[props.attributes[it].title]) as HTMLInputElement).value.trim()
//        }
//        props.onUpdate(fromPropsArray(updatedEmployee,props.employee._links))
//        for(attr in props.attributes){
//            (findDOMNode(this.refs[attr.title]) as HTMLInputElement).value = ""
//        }
//        window.location.href = "#"
//    }
//}
//
//fun RBuilder.UpdateDialog(attributes: List<Property>, employee: JsonEmployee, onUpdate:(JsonEmployee)->Unit)
//        = child(UpdateDialog::class) {
//    attrs.attributes = attributes
//    attrs.onUpdate = onUpdate
//    attrs.employee = employee
//}