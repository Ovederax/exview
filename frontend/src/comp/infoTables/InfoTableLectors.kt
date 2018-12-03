package comp.infoTables

import comp.items.Lector
import kotlinx.html.js.onClickFunction
import model.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import rest.*


class InfoTableLectors : RComponent<InfoTableLectors.Props, InfoTableLectors.State>() {
	var refs:dynamic = null
    private val client = Client()
    private fun serverUrl(str: String) =
            "http://localhost:8080/$str"

    private fun cleanUrl(str: String?) =
            str?.substringBefore("{")!!

    interface Props : RProps {
        var path: String
        var gotoSubPage : ()->Unit
        var pageSize: Int
    }

    init {
        state.links = Links()
        state.lectors = ArrayList()
        state.attributes = ArrayList()
        state.links = Links()
        state.page = Page()
        state.bufPage = 0
		state.cathedras = ArrayList()
		state.subjects = ArrayList()
    }

    interface State : RState {
        var attributes: List<Property>
        var lectors: List<JsonLector>
        var cathedras: List<JsonCathedra>
        var subjects: List<JsonSubject>
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
                    th { +"ФИО" }
                    th { +"Кафедры" }
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

                state.lectors.map {
                    Lector(it, i, state.cathedras, state.subjects){onDelete(it)}
					++i;
				}
            }
        }
        button { +"<<";  attrs.onClickFunction={onNavigate(state.links.first?.href)};attrs.disabled=state.links.prev?.href==null; }
        button { +"<";   attrs.onClickFunction={onNavigate(state.links.prev?.href) };attrs.disabled=state.links.prev?.href==null; }
        button { +">";   attrs.onClickFunction={onNavigate(state.links.next?.href) };attrs.disabled=state.links.next?.href==null; }
        button { +">>";  attrs.onClickFunction={onNavigate(state.links.last?.href) };attrs.disabled=state.links.next?.href==null; }
        div {
            p {
                +"Фио    "
                input {
					attrs["ref"] = "inputFIO"
				}
            }
            button { +"Добавить преподователя"; attrs.onClickFunction={
				val input = findDOMNode(refs["inputFIO"]) as HTMLInputElement
				if(input.value != "") {
					onCreate(JsonLector(input.value))
					input.value=""
				}
            } }
        }

    }

    private fun loadFromServer(pageSize: Int, call: (() -> Unit)? = null) {
        client.fetch(serverUrl(props.path + "?size=$pageSize")) { e ->
            val embed = JSON.parse<Embedded<JsonLectors>>(e)
            val profileURL = embed._links?.profile?.href ?: ""
            client.fetch(profileURL, "application/schema+json") { s ->
                val schema = JSON.parse<Schema<*>>(s)
                val props = toArray<Property>(schema.properties)
                
				state.attributes = props.toList()
                state.lectors = embed._embedded?.lectors?.toList() ?: ArrayList()
                state.links = embed._links ?: Links()
                state.pageNum = embed.page?.number ?: 0
                state.page = embed.page ?: Page()
				Loader().loadCathedrasList("http://localhost:8080/cathedras") {
					state.cathedras = it
					Loader().loadAllSubjects() { s: List<JsonSubject> ->
						state.subjects = s
						if(call != null) {
							call()
						} else {
							setState {	}
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
    private fun onDelete(lector: JsonLector){
        client.delete(lector._links.self?.href?:""){loadFromServer(props.pageSize)}
    }

    private fun onCreate(newLector: JsonLector) {
        val json = JSON.stringify(newLector)
        client.post(cleanUrl(state.links.self?.href),json) {
            loadFromServer(props.pageSize) {
				val h = state.links.last?.href
				if(h != null) {
					onNavigate(h)
				} else {
					setState { }
				}
			}
        }
    }

    private fun onNavigate(url:String?){
        if(url == null)
            return
        client.fetch(url){
            val embed = JSON.parse<Embedded<JsonLectors>>(it)
            setState{
                lectors = embed._embedded?.lectors?.toList()?:ArrayList()
                links = embed._links ?: Links()
                pageNum = embed.page?.number ?: 0
                page = embed.page ?: Page()
            }
        }
    }
}

fun RBuilder.InfoTableLectors(path: String, pageSize: Int, gotoSubPage : ()->Unit) = child(InfoTableLectors::class) {
    attrs.path = path
    attrs.gotoSubPage = gotoSubPage
    attrs.pageSize = pageSize
}

