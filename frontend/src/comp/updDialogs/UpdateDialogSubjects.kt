package comp.updDialogs

import kotlinx.html.js.onClickFunction
import model.*
import react.*
import react.dom.*
import rest.*
import kotlin.browser.window
import org.w3c.dom.*
import org.w3c.dom.events.Event

class UpdateDialogSubjects: RComponent<UpdateDialogSubjects.Props, UpdateDialogSubjects.State>() {
    var refs:dynamic = null

    interface Props : RProps{
        var onUpdate : (List<JsonSubject>)->Unit
        var num : Int
		var allSubjects: List<JsonSubject>
		var lectorSubjects: List<JsonSubject>
		var jsonLector: JsonLector
    }
	
	interface State : RState {
		var updateList: MutableList<JsonSubject>
	}
	
	init {
		state.updateList = ArrayList()
	}

    fun handleSubmit(e: Event) {
        e.preventDefault()
        props.onUpdate(state.updateList)
        window.location.href = "#"
    }

	fun reload() {
		Loader().getLectorSubjects(props.jsonLector) { it ->
			setState {
				updateList = ArrayList()
				updateList.addAll(it)
			}
		}
	}
	
	override fun componentDidMount() {
		reload()
	}

    override fun RBuilder.render() {
		div {
            a("#updSubjects_" + props.num.toString()) {
                +"Изменить"
            }
            div("modalDialog") {
                attrs["id"] = "updSubjects_" + props.num.toString()
                div {
                    a("#", classes = "close") { 
						+"X"
						attrs.onClickFunction = {
							reload()
						}
					}
                    h3 { +"Предметы преподователя" }
					table { tbody {
						for(i in 0..state.updateList.size-1) {
							tr {
								td {
									+state.updateList[i].name
								}
								td {
									button {
										+"X"
										attrs.onClickFunction = {
											state.updateList.removeAt(i)
											setState{}
										}
									}
								}
							}
						}
					}}
					h4 { +"Доступно для добавления" }
					for(i in 0..props.allSubjects.size-1) {
						var toDraw = true
						for(j in 0..state.updateList.size-1) {
							if(props.allSubjects[i].name == state.updateList[j].name) {
								toDraw = false
								break
							}	
						}
						if(toDraw) {
							table { tbody {
								tr {
									td {
										+props.allSubjects[i].name
									}
									td {
										button {
											+"+"
											attrs.onClickFunction = {
												state.updateList.add(props.allSubjects[i])
												setState{}
											}
										}
									}
								}
							}}
						}
					}
					p {
						button {
							attrs { onClickFunction = { handleSubmit(it) } }
								+"Применить"
						}
					}
                }
            }
        }
    }
}


fun RBuilder.UpdateDialogSubjects(jsonLector: JsonLector, num: Int, allSubjects: List<JsonSubject>, onUpdate:(List<JsonSubject>)->Unit)
        = child(UpdateDialogSubjects::class) {
    attrs.jsonLector = jsonLector
    attrs.onUpdate = onUpdate
    attrs.num = num
    attrs.allSubjects = allSubjects
}
