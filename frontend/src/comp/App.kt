package comp

import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import rest.*
import comp.infoTables.*
import kotlin.browser.window

class App : RComponent<RProps, App.State>() {
    private val client = Client()
    init {
        state.pageShow = PageShow.INFO
    }
    enum class PageShow {
        INFO, SESSION, EXAMEN
    }

    interface State: RState {
        var pageShow: PageShow
		var gotoSession: Boolean
		var gotoPage: PageShow
    }
	
	init {
		state.gotoSession = false
		state.gotoPage = PageShow.INFO
	}
	fun onConfirm(accept: Boolean) {
		window.location.href = "#"
		if(accept) {
			setState{ pageShow = gotoPage}
		}
	}
    private fun showInformation() {
        setState {
			pageShow = PageShow.INFO
		}
    }

    private fun showSessionInfo() {
        state.gotoPage = PageShow.SESSION
		window.location.href = "#gotoSession"
	}

    private fun showExamWidget() {
        state.gotoPage = PageShow.EXAMEN
		window.location.href = "#gotoSession"
    }
    override fun RBuilder.render() {
		div (classes = "content") {
			div (classes = "topMenu") {
				button { +"Справка";    attrs.onClickFunction = { showInformation() } }
				button { +"Сессия";     attrs.onClickFunction = { showSessionInfo() } }
				button { +"Экзамены";   attrs.onClickFunction = { showExamWidget() } }
			}
			div (classes = "lectors") {
				when(state.pageShow) {
					PageShow.INFO -> {
						InfoList()
					}
					PageShow.SESSION -> {
						InfoTableStudentGroups("studentsGroups", 0, false)
					}
					PageShow.EXAMEN -> {
						Examen()
					}
				}
			}
			ChoseSession(::onConfirm)
		}
    }


}

fun RBuilder.app() = child(App::class) {}
