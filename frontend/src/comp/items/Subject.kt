package comp.items

import kotlinx.html.js.onClickFunction
import model.JsonSubject
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.td
import react.dom.tr

class Subject: RComponent<Subject.Props, RState>(){
    interface Props: RProps {
        var jsonSubject: JsonSubject
        var key:String
        var onDelete:()->Unit
		var num: Int
    }
    override fun RBuilder.render() {
        tr {
            td { +props.num.toString() }
            td { +props.jsonSubject.name }
			td {
                button {
                    attrs { onClickFunction = {
                            +props.jsonSubject._links.lectors!!.href
                        }}
                    +"Просмотр"
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
