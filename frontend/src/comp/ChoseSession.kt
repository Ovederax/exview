package comp

import comp.items.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import rest.*
import org.w3c.dom.events.Event
import model.*
import kotlin.browser.window
import kotlin.js.Date
import kotlinx.html.js.*
import org.w3c.dom.HTMLSelectElement

class ChoseSession: RComponent<ChoseSession.Props, ChoseSession.State>() {
    var refs:dynamic = null

	interface Props:RProps {
		var onAccept: (Boolean)->Unit
    }
	
	init {
		state.strBeg = "-"
		state.strEnd = "-"
	}

	interface State: RState {
		var strBeg: String
		var strEnd: String
	}
	
	override fun componentDidMount() {
	
    }


	override fun RBuilder.render() {
		div("modalDialog") { 
		attrs["id"] = "gotoSession"
		div {
			a("#", classes = "close") { 
				+"X"
				attrs.onClickFunction = { props.onAccept(false) }
			}
			h2 { +"Выбор сессии для редактирования"	}
			select(classes = "selectClasses") {
				attrs["ref"] = "selectSession"
				attrs.onChangeFunction = {
					val select = findDOMNode(refs["selectSession"]) as HTMLSelectElement
					val id = select.selectedIndex
					if(id == 0) {
						state.strBeg = "-"
						state.strEnd = "-"
					} else {
						//state.selectedSubject = state.subjectsToSelect[id-1]
						state.strBeg = "7 января"
						state.strEnd = "27 января"
					}
					setState {}
				}
				option {
					+"-"
				}
				option {
					+"Зимний семестр 2019г."
				}
			}
			table { tbody {
				tr {
					td { +"Начало:"	}
					td { +state.strBeg }
				}
				tr {
					td { +"Окончание:"	}
					td { +state.strEnd }
					td {
						button {
							+"Перейти"
							attrs.onClickFunction = { 
								val select = findDOMNode(refs["selectSession"]) as HTMLSelectElement
								val id = select.selectedIndex
								if(id != 0) {
									props.onAccept(true)
								}
							}
						}
					}
				}
				tr{ td{ attrs["colSpan"] = "3"; h2 { +"Добавить новую сессию"} } }
				tr {
					td { +"Название" }
					td { input {  } }
				}
				tr {
					td { +"Начало" }
					td { }
				}
				tr {
					td { +"День" }
					td { input {  } }
				}
				tr {
					td { +"Месяц" }
					td { input {  } }
				}
				tr {
					td { +"Год" }
					td { input {  } }
				}
				tr {
					td { +"Окончание" }
					td { }
				}
				tr {
					td { +"День" }
					td { input {  } }
				}
				tr {
					td { +"Месяц" }
					td { input {  } }
				}
				tr {
					td { +"Год" }
					td { input {  } }
					td {
						button {
							+"Добавить"
							attrs.onClickFunction = {
								window.alert("Недостаточно прав.\n"+
											"Пользователь: Альтман Е.А.\n" +
											"Не удалось добавить новую сессию."
								)
							}
						}
					}
				}
			}}
		}}
	}
}

fun RBuilder.ChoseSession(onAccept:(Boolean)->Unit) = child(ChoseSession::class) {
	attrs.onAccept = onAccept
}







