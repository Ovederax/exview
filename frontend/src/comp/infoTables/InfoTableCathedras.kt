package comp.infoTables

import comp.items.Cathedra
import kotlinx.html.js.onClickFunction
import model.JsonCathedra
import model.JsonCathedras
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import rest.*



class InfoTableCathedras : RComponent<InfoTableCathedras.Props, InfoTableCathedras.State>() {
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
        state.cathedras = ArrayList()
        state.attributes = ArrayList()
        state.links = Links()
        state.page = Page()
        state.bufPage = 0
    }

    interface State : RState {
        var attributes: List<Property>
        var cathedras: List<JsonCathedra>
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
                    th { +"Аудитории" }
                    th { +"Предметы" }
                    th { +"Факультеты" }
                    th { +"Преподователи" }
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

                state.cathedras.map {
                    Cathedra(it, i){onDelete(it)}
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
                +"Название кафедры  "
                input {
					attrs["ref"] = "inputName"
				}
            }
            button { +"Добавить кафедру"; attrs.onClickFunction={
				val input = findDOMNode(refs["inputName"]) as HTMLInputElement
				onCreate(JsonCathedra(input.value))
            } }
        }

    }

    private fun loadFromServer(pageSize: Int, call: () -> Unit = {}) {
        client.fetch(url(props.path + "?size=$pageSize")) { e ->
            val embed = JSON.parse<Embedded<JsonCathedras>>(e)
            val profileURL = embed._links?.profile?.href ?: ""
            client.fetch(profileURL, "application/schema+json") { s ->
                val schema = JSON.parse<Schema<*>>(s)
                val props = toArray<Property>(schema.properties)
                setState {
                    attributes = props.toList()

                    cathedras = embed._embedded?.cathedras?.toList() ?: ArrayList()
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
    private fun onDelete(cathedra: JsonCathedra){
        client.delete(cathedra._links.self?.href?:""){loadFromServer(props.pageSize)}
    }

    private fun onCreate(item: JsonCathedra) {
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
            val embed = JSON.parse<Embedded<JsonCathedras>>(it)
            setState{
                cathedras = embed._embedded?.cathedras?.toList()?:ArrayList()
                links = embed._links ?: Links()
                pageNum = embed.page?.number ?: 0
                page = embed.page ?: Page()
            }
        }
    }
}

fun RBuilder.InfoTableCathedras(path: String, pageSize: Int, gotoSubPage : ()->Unit) = child(InfoTableCathedras::class) {
    attrs.path = path
    attrs.gotoSubPage = gotoSubPage
    attrs.pageSize = pageSize
}

