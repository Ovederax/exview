package comp.infoTables

import comp.items.*
import kotlinx.html.js.onClickFunction
import model.*
import org.w3c.dom.*
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import rest.*


class InfoTableStudentGroups : RComponent<InfoTableStudentGroups.Props, InfoTableStudentGroups.State>() {
    var refs:dynamic = null
	private val client = Client()
    private fun url(str: String) =
            "http://localhost:8080/$str"

    private fun cleanUrl(str: String?) =
            str?.substringBefore("{")!!

    interface Props : RProps {
        var path: String
        var pageSize: Int
        var showAddBtn: Boolean
    }

    init {
        state.links = Links()
        state.studentsGroups = ArrayList()
        state.subjects = ArrayList()
        state.lectorsMap = HashMap()
        state.links = Links()
        state.page = Page()
        state.bufPage = 0
    }

    interface State : RState {
        var studentsGroups: List<JsonStudentGroup>
        var subjects: List<JsonSubject>
        var lectorsMap: MutableMap<JsonSubject, List<JsonLector>>
        var links: Links
        var pageNum: Int
        var page: Page
        var bufPage: Int
    }

    override fun componentDidMount() {
        loadFromServer(props.pageSize)
    }

    override fun componentDidUpdate(prevProps: Props, prevState: State, snapshot: Any) {
        if(state.bufPage != props.pageSize) {
            loadFromServer(props.pageSize)
            state.bufPage = props.pageSize
        }
    }
    override fun RBuilder.render() {
        table {
            tbody {
                tr {
                    th { +"N" }
                    th { +"Группа" }
                    th { +"Предметы" }
                    th { +"Удалить" }
                }
                var i = 1
                if(state.page != null) {
                    var a = state.page.number
                    var b = state.page.size
                    if(a != null && b!=null) {
                        i += a*b
                    }
                }
                state.studentsGroups.map {
                    StudentGroup(it, i, state.subjects, state.lectorsMap){onDelete(it)}
					++i;
				}
            }
        }
		if(props.pageSize != 0) {
			button { +"<<";  attrs.onClickFunction={onNavigate(state.links.first?.href)};attrs.disabled=state.links.prev?.href==null; }
			button { +"<";   attrs.onClickFunction={onNavigate(state.links.prev?.href) };attrs.disabled=state.links.prev?.href==null; }
			button { +">";   attrs.onClickFunction={onNavigate(state.links.next?.href) };attrs.disabled=state.links.next?.href==null; }
			button { +">>";  attrs.onClickFunction={onNavigate(state.links.last?.href) };attrs.disabled=state.links.next?.href==null; }
		}

		if(props.showAddBtn) {
			div {
				p {
					+"Группа  "
					input {
						attrs["ref"] = "inputName"
					}
				}
				button { +"Создать группу"; attrs.onClickFunction={
					val input = findDOMNode(refs["inputName"]) as HTMLInputElement
					onCreate(JsonStudentGroup(input.value))
				} }
			}
		}
    }

    private fun loadFromServer(pageSize: Int, call: () -> Unit = {}) {
        client.fetch(url(props.path + "?size=$pageSize")) { e ->
            val embed = JSON.parse<Embedded<JsonStudentGroups>>(e)
            val profileURL = embed._links?.profile?.href ?: ""
            client.fetch(profileURL, "application/schema+json") { s ->
                val schema = JSON.parse<Schema<*>>(s)
                val props = toArray<Property>(schema.properties)
				state.studentsGroups = embed._embedded?.studentsGroups?.toList() ?: ArrayList()
				state.links = embed._links ?: Links()
				state.pageNum = embed.page?.number ?: 0
				Loader().loadAllSubjects() {
					state.subjects = it
					var counter = 0
					var size = state.subjects.size-1
					for(i in 0..size) {
						Loader().getLectorsBySubject(state.subjects[i]) {
							state.lectorsMap.put(state.subjects[i], it)
							counter += 1
							if(counter == state.subjects.size) {
								setState {}
								call()
							}
						}
					}
				}
            }
        }
    }


    private fun handleNav(e: Event, href: String) {
        e.preventDefault()
        onNavigate(href)
    }
    private fun onDelete(item: JsonStudentGroup){
        client.delete(item._links.self?.href?:""){loadFromServer(props.pageSize)}
    }

    private fun onCreate(item: JsonStudentGroup) {
        val json = JSON.stringify(item)

        client.post(cleanUrl(state.links.self?.href),json) {
            loadFromServer(props.pageSize) {
                    val h = state.links.last?.href
					if(h != null) {
						onNavigate(h)
					}
                }
        }
    }

    private fun onNavigate(url:String?){
        if(url == null)
            return
        client.fetch(url){
            val embed = JSON.parse<Embedded<JsonStudentGroups>>(it)
            setState{
                studentsGroups = embed._embedded?.studentsGroups?.toList()?:ArrayList()
                links = embed._links ?: Links()
                pageNum = embed.page?.number ?: 0
                page = embed.page ?: Page()
            }
        }
    }
}

fun RBuilder.InfoTableStudentGroups(path: String, pageSize: Int, showAddBtn: Boolean = true) = child(InfoTableStudentGroups::class) {
    attrs.path = path
    attrs.pageSize = pageSize
    attrs.showAddBtn = showAddBtn
}
