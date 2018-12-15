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
	}

	fun reloadData() {
		Loader().getCathedraByLector(props.jsonLector)
		{ it ->
			state.cathedra = it
			setState{}
		}
	}
	
	override fun componentDidMount() {
		reloadData()
	}
	
	
    override fun RBuilder.render() {
        tr {
            td { +props.num.toString() }
            td { +props.jsonLector.name }
            td {
                UpdateDialogCathedras(props.num, props.cathedras, state.cathedra, props.jsonLector.name) {
					//Установка кафедры лектору
					Loader().setCathedraInLector(PairLectorCathedra(props. jsonLector, it)) {
						reloadData()
					}
                }
            }
            td {
				//props.jsonLector._links.subjects?.href, 
				UpdateDialogSubjects(props.jsonLector,props.num, props.allSubjects) { it ->
					//добавление предметов лектору
					val pair = PairLectorSubjects()
					pair.lector = props.jsonLector
					pair.subjects = it
					Loader().setSubjectsInLector(pair) {
						reloadData()
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
fun RBuilder.Lector(jsonLector: JsonLector, num: Int, cathedras: List<JsonCathedra>, allSubjects: List<JsonSubject>, onDelete:()->Unit = {})
        = child(Lector::class) {
    attrs.jsonLector = jsonLector
    attrs.onDelete = onDelete
    attrs.num = num
    attrs.cathedras = cathedras
    attrs.allSubjects = allSubjects
}
