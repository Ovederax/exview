package comp

import kotlinx.html.js.onClickFunction
import model.JsonContent
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.td
import react.dom.tr
import rest.JsonEmployee

class Content : RComponent<Content.Props, RState>(){
    interface Props:RProps{
        var content: JsonContent
        var key:String
        var onDelete:()->Unit
		var num: Int
    }
    override fun RBuilder.render() {
        tr {
			td { +props.num.toString()}
            td { +props.content.name }
            td {
                button {
                    attrs { onClickFunction = {
                        props.onDelete() } }
                    +"Delete"
                }
            }
        }
    }
}
fun RBuilder.Content(content: JsonContent, num: Int, key: String, onDelete:()->Unit)
        = child(Content::class) {
    attrs.content = content
    attrs.key = key
    attrs.onDelete = onDelete
	attrs.num = num;
}
