package comp.updDialogs

import kotlinx.html.js.onClickFunction
import model.JsonCathedra
import model.JsonStudentGroup
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import kotlin.browser.window

class SessionSubjectAddDialog : RComponent<SessionSubjectAddDialog.Props, RState>() {
    var refs:dynamic = null

    interface Props : RProps {
        var group: JsonStudentGroup
        var num: Int
    }

    fun handleSubmit(e: Event) {
        e.preventDefault()
        window.location.href = "#"
    }

    override fun RBuilder.render() {
        div {
            a("#setSubj_" + props.num.toString()) {
                +"Просмотр"
            }
            div("modalDialog") {
                attrs["id"] = "setSubj_" + props.num.toString()
                div {
                    a("#", classes = "close") { +"X" }
                    h2 { +"Создание сессионных предметов группы ${props.group.name}" }

                    table {  }
                    /*p {
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
                    }*/

                    button {
                        attrs { onClickFunction = { handleSubmit(it) } }
                        +"Применить"
                    }
                }
            }
        }
    }
}


fun RBuilder.SessionSubjectAddDialog(group: JsonStudentGroup, num: Int)
        = child(SessionSubjectAddDialog::class) {
    attrs.group = group
    attrs.num = num
}