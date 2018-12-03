package comp.items

import kotlinx.html.js.onClickFunction
import model.JsonCathedra
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.td
import react.dom.tr

class Cathedra: RComponent<Cathedra.Props, RState>(){
    interface Props: RProps {
        var jsonCathedra: JsonCathedra
        var key:String
        var onDelete:()->Unit
		var num: Int
    }
    override fun RBuilder.render() {
        tr {
            td { +props.num.toString() }
            td { +props.jsonCathedra.name }
            td {
                button {
                    attrs { onClickFunction = {
                            +props.jsonCathedra._links.auditorium!!.href
                        }}
                    +"Просмотр"
                }
            }
			td {
                button {
                    attrs { onClickFunction = {
                            +props.jsonCathedra._links.subjects!!.href
                        }}
                    +"Просмотр"
                }
            }
			td {
                button {
                    attrs { onClickFunction = {
                            +props.jsonCathedra._links.lectors!!.href
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
fun RBuilder.Cathedra(jsonCathedra: JsonCathedra, num: Int, onDelete:()->Unit = {})
        = child(Cathedra::class) {
    attrs.jsonCathedra = jsonCathedra
    attrs.onDelete = onDelete
    attrs.num = num
}
