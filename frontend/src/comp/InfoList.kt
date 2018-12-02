package comp

import comp.infoTables.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.JsonLector
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.dom.findDOMNode
import rest.Client

class InfoList : RComponent<InfoList.Props, InfoList.State>() {
    var refs: dynamic = null
    val client = Client()
    private fun url(path: String) : String {
        return "localhost:8080/"+path
    }
    init {
        state.infoType =InfoType.LECTORS
        state.lectors = ArrayList()
        state.path =  ""
		state.pageSize = 10
    }

    interface Props: RProps {

    }

    interface State: RState {
        var infoType: InfoType
        var lectors: List<JsonLector>
        var path: String
        var pageSize: Int
    }

    enum class InfoType(val url: String) {
        NONE(""),
        LECTORS("lectors"),
        STUDENT_GROUPS("studentsGroups"),
        SUBJECTS("subjects"),
        CATHEDRAS("cathedras"),
        FACULTETS("facultets"),
        AUDITORIUM("auditotiums")
    }

    fun gotoSubPage() {

    }

    fun setPath(typeInfo: InfoType) {
        setState {
			infoType = typeInfo
            path = url(typeInfo.url)
        }
    }
    override fun RBuilder.render() {
        div(classes="InfoListDiv") {
            button (classes = "materialBtn") {
                +"Преподователи"
                attrs.onClickFunction = {
                    setPath(InfoType.LECTORS)
                }
            }
            button(classes = "materialBtn") {
                +"Кафедры"
                attrs.onClickFunction = {
                    setPath(InfoType.CATHEDRAS)
                }
            }
            button(classes = "materialBtn") {
                +"Предметы"
                attrs.onClickFunction = {
                    setPath(InfoType.SUBJECTS)
                }
            }
            button(classes = "materialBtn") {
                +"Группы"
                attrs.onClickFunction = {
                    setPath(InfoType.STUDENT_GROUPS)
                }
            }
        }
		p {
			+"Количествово записей:  "
			input {
				attrs.onChangeFunction = { e->
					var s = (js("e.target.value") as String)
					if(s=="") {
						s = "0"
					}
					try {
						onUpdatePageSize(s.toInt())
					}catch (ex : Exception) {
					}
				}
			}
		}
        
        when(state.infoType) {
            InfoType.LECTORS -> {
                InfoTableLectors(InfoType.LECTORS.url, state.pageSize) { gotoSubPage() }
            }
            InfoType.SUBJECTS -> {
                InfoTableSubjects(InfoType.SUBJECTS.url, state.pageSize) { gotoSubPage() }
            }
            InfoType.CATHEDRAS -> {
				InfoTableCathedras(InfoType.CATHEDRAS.url, state.pageSize) { gotoSubPage() }
            }
			InfoType.STUDENT_GROUPS -> {
				InfoTableStudentGroups(InfoType.STUDENT_GROUPS.url, state.pageSize)
            }
        }

    }
    private fun onUpdatePageSize(size: Int) {
        if(size!=state.pageSize) {
            setState {
                this.pageSize = size
            }
        }
    }
}


fun RBuilder.InfoList() = child(InfoList::class) {

}

