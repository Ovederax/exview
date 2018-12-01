package comp.infoTables

import comp.items.SessionSubject
import kotlinx.html.js.onClickFunction
import model.JsonSessionSubject
import model.JsonSessionSubjects
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import rest.*

// /sessionSubjects
class InfoTableSessionSubject : RComponent<InfoTableSessionSubject.Props, InfoTableSessionSubject.State>() {
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
        state.sessionSubjects = ArrayList()
        state.attributes = ArrayList()
        state.links = Links()
        state.page = Page()
        state.bufPage = 0
    }

    interface State : RState {
        var attributes: List<Property>
        var sessionSubjects: List<JsonSessionSubject>
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
                    th { +"Предмет" }
                    th { +"Преподователь" }
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

                state.sessionSubjects.map {
                    SessionSubject(it, i/*, it._links.self?.href ?: ""*/){onDelete(it)}
					++i;
				}
            }
        }
        div {
            /*p {
                +"Фио    "
                input {
					attrs["ref"] = "inputFIO"
				}
            }
            button { +"Добавить преподователя"; attrs.onClickFunction={
				val input = findDOMNode(refs["inputFIO"]) as HTMLInputElement
				onCreate(JsonSessionSubject(input.value))
            } }*/
        }

    }

    private fun loadFromServer(pageSize: Int, call: (() -> Unit)? = null) {
        client.fetch(serverUrl(props.path + "?size=$pageSize")) { e ->
            val embed = JSON.parse<Embedded<JsonSessionSubjects>>(e)
            val profileURL = embed._links?.profile?.href ?: ""
            client.fetch(profileURL, "application/schema+json") { s ->
                val schema = JSON.parse<Schema<*>>(s)
                val props = toArray<Property>(schema.properties)
                
				state.attributes = props.toList()
                state.sessionSubjects = embed._embedded?.sessionSubjects?.toList() ?: ArrayList()
                state.links = embed._links ?: Links()
                state.pageNum = embed.page?.number ?: 0
                state.page = embed.page ?: Page()
				if(call != null) {
					call()
				} else {
					setState {	}
				}
				
            }
        }
    }

    private fun handleNav(e: Event, href: String) {
        e.preventDefault()
        onNavigate(href)
    }
    private fun onDelete(item: JsonSessionSubject){
        client.delete(item._links.self?.href?:""){loadFromServer(props.pageSize)}
    }

    private fun onCreate(item: JsonSessionSubject) {
        val json = JSON.stringify(item)

        client.post(cleanUrl(state.links.self?.href),json) {
            loadFromServer(props.pageSize) {
                    val h = state.links.last?.href?:""
                    onNavigate(h)
                }
        }
    }

    private fun onNavigate(url:String?){
        if(url == null)
            return
        client.fetch(url){
            val embed = JSON.parse<Embedded<JsonSessionSubjects>>(it)
            setState{
                sessionSubjects = embed._embedded?.sessionSubjects?.toList()?:ArrayList()
                links = embed._links ?: Links()
                pageNum = embed.page?.number ?: 0
                page = embed.page ?: Page()
            }
        }
    }
}




fun RBuilder.InfoTableSessionSubject(path: String, pageSize: Int, gotoSubPage : ()->Unit) = child(InfoTableSessionSubject::class) {
    attrs.path = path
    attrs.gotoSubPage = gotoSubPage
    attrs.pageSize = pageSize
}


