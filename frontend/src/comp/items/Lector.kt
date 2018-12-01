package comp.items

import comp.updDialogs.UpdateDialogCathedras
import kotlinx.html.js.onClickFunction
import model.JsonCathedra
import model.JsonCathedras
import model.JsonLector
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.td
import react.dom.tr
import rest.*

class Lector: RComponent<Lector.Props, RState>(){
    interface Props: RProps {
        var jsonLector: JsonLector
        var key:String
        var onDelete:()->Unit
		var num: Int
		var cathedras: List<JsonCathedra>
    }
    override fun RBuilder.render() {
        tr {
            td { +props.num.toString() }
            td { +props.jsonLector.name }
            td {
                UpdateDialogCathedras(props.jsonLector._links.cathedra?.href, props.num, props.cathedras) {
					//отправить добавление на props.jsonLector._links.cathedra?.href + cathedra
					//отправить добавление на it.self + lector
					var client = Client()
					var send = JSON.stringify(props.jsonLector)
					client.post("http://localhost:8080/lectors", send) {
						console.log(it)
					}
                }
            }
            td {
                button {
                    attrs { onClickFunction = {
                            +props.jsonLector._links.subjects!!.href
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
fun RBuilder.Lector(jsonLector: JsonLector, num: Int, cathedras: List<JsonCathedra>, onDelete:()->Unit = {})
        = child(Lector::class) {
    attrs.jsonLector = jsonLector
    attrs.onDelete = onDelete
    attrs.num = num
    attrs.cathedras = cathedras
}
