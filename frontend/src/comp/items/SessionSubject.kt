package comp.items

import kotlinx.html.js.onClickFunction
import model.JsonCathedra
import model.JsonCathedras
import model.JsonSessionSubject
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.td
import react.dom.tr
import rest.*

class SessionSubject: RComponent<SessionSubject.Props, RState>(){
    interface Props: RProps {
        var jsonSubj: JsonSessionSubject
        var key:String
        var onDelete:()->Unit
		var num: Int
    }
    override fun RBuilder.render() {
        tr {
            td { +props.num.toString() }
            //td { +props.jsonSubj.name }
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
fun RBuilder.SessionSubject(jsonSubj: JsonSessionSubject, num: Int, onDelete:()->Unit = {})
        = child(SessionSubject::class) {
    attrs.jsonSubj = jsonSubj
    attrs.onDelete = onDelete
    attrs.num = num
}
