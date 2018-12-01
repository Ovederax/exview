package comp

import kotlinx.html.js.onClickFunction
import react.*
import react.dom.button
import react.dom.div
import rest.Client


class App : RComponent<RProps, App.State>() {
    private val client = Client()
    init {
        state.pageShow = PageShow.INFO
    }
//    private fun url(str:String) =
//            "http://localhost:8080/$str"
//    private fun cleanUrl(str:String?) =
//            str?.substringBefore("{")!!
//
//    init {
//        state.apply {
//            lectors = ArrayList()
//            attributes = ArrayList()
//            links = Links()
//            pageSize = 2
//        }
//    }
    enum class PageShow {
        INFO, SESSION, EXAMEN
    }

    interface State:RState {
        var pageShow: PageShow
//        var attributes: List<Property>
//        var lectors: List<JsonLector>
//        var links:Links
//        var pageSize:Int
//        var page: Page
    }

    override fun componentDidMount() {
 
//        loadFromServer(state.pageSize)
    }

    private fun showInformation() {
        setState {
            pageShow = PageShow.INFO
        }
    }

    private fun showSessionInfo() {
        setState {
            pageShow = PageShow.SESSION
        }
    }

    private fun showExamWidget() {
        setState {
            pageShow = PageShow.EXAMEN
        }
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
						+"2"
					}
					PageShow.EXAMEN -> {
						+"3"
					}
				}
			}
		}


//        CreateDialog(state.attributes, ::onCreate)
//        EmployeeList(state.lectors, state.links,state.page, state.pageSize, ::onNavigate, ::onDelete, ::onUpdatePageSize)

    }


}

fun RBuilder.app() = child(App::class) {}
