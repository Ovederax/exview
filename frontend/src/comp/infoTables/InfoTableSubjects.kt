package comp.infoTables

import comp.items.Subject
import kotlinx.html.js.onClickFunction
import model.JsonSubject
import model.JsonSubjects
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import rest.*


class InfoTableSubjects : RComponent<InfoTableSubjects.Props, InfoTableSubjects.State>() {
   	var refs:dynamic = null
	private val client = Client()
    private fun url(str: String) =
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
        state.subjects = ArrayList()
        state.attributes = ArrayList()
        state.links = Links()
        state.page = Page()
        state.bufPage = 0
    }

    interface State : RState {
        var attributes: List<Property>
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
                    th { +"Название" }
                    th { +"Преподователи" }
                    th { +"Группы" }
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

                state.subjects.map {
                    Subject(it, i){onDelete(it)}
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
                +"Название предмета  "
                input {
					attrs["ref"] = "inputName"
				}
            }
            button { +"Создать новый предмет"; attrs.onClickFunction={
				val input = findDOMNode(refs["inputName"]) as HTMLInputElement
				onCreate(JsonSubject(input.value))
            } }
        }

    }

    private fun loadFromServer(pageSize: Int, call: () -> Unit = {}) {
        client.fetch(url(props.path + "?size=$pageSize")) { e ->
            val embed = JSON.parse<Embedded<JsonSubjects>>(e)
            val profileURL = embed._links?.profile?.href ?: ""
            client.fetch(profileURL, "application/schema+json") { s ->
                val schema = JSON.parse<Schema<*>>(s)
                val props = toArray<Property>(schema.properties)
                setState {
                    attributes = props.toList()
                    subjects = embed._embedded?.subjects?.toList() ?: ArrayList()
                    links = embed._links ?: Links()
                    pageNum = embed.page?.number ?: 0
                    call()
                }

            }
        }
    }


    private fun handleNav(e: Event, href: String) {
        e.preventDefault()
        onNavigate(href)
    }
    private fun onDelete(subject: JsonSubject){
        client.delete(subject._links.self?.href?:""){loadFromServer(props.pageSize)}
    }

    private fun onCreate(newSubject: JsonSubject) {
        val json = JSON.stringify(newSubject)

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
            val embed = JSON.parse<Embedded<JsonSubjects>>(it)
            setState{
                subjects = embed._embedded?.subjects?.toList()?:ArrayList()
                links = embed._links ?: Links()
                pageNum = embed.page?.number ?: 0
                page = embed.page ?: Page()
            }
        }
    }
}


fun RBuilder.InfoTableSubjects(path: String, pageSize: Int, gotoSubPage : ()->Unit) = child(InfoTableSubjects::class) {
    attrs.path = path
    attrs.gotoSubPage = gotoSubPage
    attrs.pageSize = pageSize
}

