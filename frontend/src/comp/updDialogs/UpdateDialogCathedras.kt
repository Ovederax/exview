package comp.updDialogs

import kotlinx.html.js.onClickFunction
import model.*
import react.*
import react.dom.*
import rest.*
import kotlin.browser.window
import org.w3c.dom.*
import org.w3c.dom.events.Event

class UpdateDialogCathedras: RComponent<UpdateDialogCathedras.Props, RState>() {
    var refs:dynamic = null

    interface Props : RProps{
        var onUpdate : (JsonCathedra?)->Unit
        var num : Int
		var cathedras: List<JsonCathedra>
		var cathedra: JsonCathedra?
    }

    fun handleSubmit(e: Event) {
        e.preventDefault()
        val select = findDOMNode(this.refs["selectCathedra"]) as HTMLSelectElement
        val id = select.selectedIndex
		if(id == 0) {
            props.onUpdate(null)
        } else {
			val cathetra = props.cathedras[id-1]
            props.onUpdate(cathetra)
        }
		select.selectedIndex = 0
        window.location.href = "#"
    }

    override fun RBuilder.render() {
        div {
            a("#updCathedra_" + props.num.toString()) {
                +"Изменить"
            }
            div("modalDialog") {
                attrs["id"] = "updCathedra_" + props.num.toString()
                div {
                    a("#", classes = "close") { 
						+"X"
						attrs.onClickFunction = {
						    val select = findDOMNode(refs["selectCathedra"]) as HTMLSelectElement
							select.selectedIndex = 0
						}
					}
					var name = props.cathedra?.name ?: "не установлена"
                    h3 { +"Кафедра преподователя" }
					p { +"На данный момент $name"}
					p {
                        select(classes = "selectClasses") {
                            attrs["ref"] = "selectCathedra"
                            option {
                                +"(пусто)"
                            }
                            props.cathedras.map { prop ->
                                option {
                                    +prop.name
                                }
                            }
                        }
                    }
					
					button {
                        attrs { onClickFunction = { handleSubmit(it) } }
                            +"Применить"
                    }
                }
            }
        }
    }
}


fun RBuilder.UpdateDialogCathedras(num: Int, cathedras: List<JsonCathedra>, cathedra: JsonCathedra?, onUpdate:(JsonCathedra?)->Unit)
        = child(UpdateDialogCathedras::class) {
    attrs.onUpdate = onUpdate
    attrs.num = num
    attrs.cathedras = cathedras
    attrs.cathedra = cathedra
}
