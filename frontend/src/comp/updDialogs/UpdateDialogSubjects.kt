package comp.updDialogs

import kotlinx.html.js.onClickFunction
import model.*
import react.*
import react.dom.*
import rest.*
import kotlin.browser.window
import org.w3c.dom.*
import org.w3c.dom.events.Event

class UpdateDialogSubjects: RComponent<UpdateDialogSubjects.Props, RState>() {
    var refs:dynamic = null

    interface Props : RProps{
        var onUpdate : (List<JsonSubject>)->Unit
        var num : Int
		var allSubjects: List<JsonSubject>
		var lectorSubjects: List<JsonSubject>
    }

    fun handleSubmit(e: Event) {
        /*e.preventDefault()
        val select = findDOMNode(this.refs["selectCathedra"]) as HTMLSelectElement
        val id = select.selectedIndex
		if(id == 0) {
            props.onUpdate(null)
        } else {
			val cathetra = props.allSubjects[id-1]
            props.onUpdate(cathetra)
        }
		select.selectedIndex = 0
        window.location.href = "#"*/
    }

    override fun RBuilder.render() {
        div {
            a("#updSubjects_" + props.num.toString()) {
                +"Просмотр"
            }
            div("modalDialog") {
                attrs["id"] = "updSubjects_" + props.num.toString()
                div {
                    a("#", classes = "close") { 
						+"X"
						/*attrs.onClickFunction = {
						    val select = findDOMNode(refs["selectCathedra"]) as HTMLSelectElement
							select.selectedIndex = 0
						}*/
					}
					/*var name = props.lectorSubjects?.name ?: "не установлена"
                    h3 { +"Предметы преподователя" }
					p { +"На данный момент $name"}
					p {
                        select(classes = "selectClasses") {
                            attrs["ref"] = "selectCathedra"
                            option {
                                +"(пусто)"
                            }
                            props.allSubjects.map { prop ->
                                option {
                                    +prop.name
                                }
                            }
                        }
                    }
					
					*/
					button {
                        attrs { onClickFunction = { handleSubmit(it) } }
                            +"Применить"
                    }
                }
            }
        }
    }
}


fun RBuilder.UpdateDialogSubjects(num: Int, allSubjects: List<JsonSubject>, lectorSubjects: List<JsonSubject>, onUpdate:(List<JsonSubject>)->Unit)
        = child(UpdateDialogSubjects::class) {
    attrs.onUpdate = onUpdate
    attrs.num = num
    attrs.allSubjects = allSubjects
    attrs.lectorSubjects = lectorSubjects
}
