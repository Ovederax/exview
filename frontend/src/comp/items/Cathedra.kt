package comp.items

import kotlinx.html.js.onClickFunction
import model.*
import react.*
import react.dom.*
import rest.*


class Cathedra: RComponent<Cathedra.Props, Cathedra.State>(){
    interface Props: RProps {
        var jsonCathedra: JsonCathedra
        var key:String
        var onDelete:()->Unit
		var num: Int
		
    }
	
	interface State: RState {
		var subjects: List<JsonSubject>
		var lectors: List<JsonLector>
		//var auditorium: List<JsonAuditorium>
	}
	
	init {
		state.subjects = ArrayList()
		state.lectors = ArrayList()
	}
	
	override fun componentDidMount() {
		Loader().getSubjectByCathedra(props.jsonCathedra) { subjects ->
			state.subjects = subjects
			Loader().getLectorsByCathedra(props.jsonCathedra) { lectors ->
				state.lectors = lectors
				setState{}
			}
		}
	}
	
    override fun RBuilder.render() {
        tr {
            td { +props.num.toString() }
            td { +props.jsonCathedra.name }
            /*td {
                button {
                    attrs { onClickFunction = {
                            //+props.jsonCathedra._links.auditoriums!!.href
                        }}
                    +"Просмотр"
                }
            }*/
			td {
				//+props.jsonCathedra._links.subjects!!.href
				div {
					a("#showSubj_"+props.num.toString()) {
						+"Просмотр"
					}
				}
				div("modalDialog") {
					attrs["id"] = "showSubj_"+props.num.toString()
					div {
						strong { p {
							+"Предметы кафедры"
						}}
						a("#", classes = "close") { 
							+"X"
						}
						table { tbody {
							for(i in 0..state.subjects.size-1) {
								tr { td {
									+state.subjects[i].name
								}}
							}
						}}
					}
				}	 
            }
			
			td {
				div {
					a("#showLectors_"+props.num.toString()) {
						+"Просмотр"
					}
				}
				div("modalDialog") {
					attrs["id"] = "showLectors_"+props.num.toString()
					div {
						strong { p {
							+"Лекторы кафедры"
						}}
						a("#", classes = "close") { 
							+"X"
						}
						table { tbody {
							for(i in 0..state.lectors.size-1) {
								tr { td {
									+state.lectors[i].name
								}}
							}
						}}
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
fun RBuilder.Cathedra(jsonCathedra: JsonCathedra, num: Int, onDelete:()->Unit = {})
        = child(Cathedra::class) {
    attrs.jsonCathedra = jsonCathedra
    attrs.onDelete = onDelete
    attrs.num = num
}
