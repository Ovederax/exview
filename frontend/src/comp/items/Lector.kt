package comp.items

import comp.updDialogs.*
import kotlinx.html.js.onClickFunction
import model.*
import react.*
import react.dom.*
import rest.*


class Lector: RComponent<Lector.Props, Lector.State>(){
    interface Props: RProps {
        var jsonLector: JsonLector
        var key:String
        var onDelete:()->Unit
		var num: Int
		var cathedras: List<JsonCathedra>
		var allSubjects: List<JsonSubject>
    }
	
	interface State: RState {
		var cathedra: JsonCathedra?
		var lectorSubjects: List<JsonSubject>
	}
	
	override fun componentDidMount() {
		Loader().getCathedraByLector(props.jsonLector)
		{ it ->
			setState {
				cathedra = it
			}
		}
	}
	
	
    override fun RBuilder.render() {
        tr {
            td { +props.num.toString() }
            td { +props.jsonLector.name }
            td {
                UpdateDialogCathedras(props.num, props.cathedras, state.cathedra) {
					//Установка кафедры лектору
					Loader().setCathedraInLector(PairLectorCathedra(props. jsonLector, it)) {
						
					}
                }
            }
            td {
				//props.jsonLector._links.subjects?.href, 
				UpdateDialogSubjects(props.num, props.allSubjects, state.lectorSubjects) {
					//добавление предметов лектору
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
