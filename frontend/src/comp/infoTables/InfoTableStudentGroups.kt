package comp.infoTables

import comp.items.StudentGroup
import kotlinx.html.js.onClickFunction
import model.JsonStudentGroup
import model.JsonStudentGroups
import model.JsonLectors
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import rest.*


class InfoTableStudentGroups : RComponent<InfoTableStudentGroups.Props, InfoTableStudentGroups.State>() {
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
        state.studentsGroups = ArrayList()
        state.attributes = ArrayList()
        state.links = Links()
        state.page = Page()
        state.bufPage = 0
    }

    interface State : RState {
        var attributes: List<Property>
        var studentsGroups: List<JsonStudentGroup>
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

        //    CreateDialog(state.attributes, ::onCreate)
        //    EmployeeList(state.employees, state.links, state.pageSize*state.pageNum, state.attributes, ::onNavigate, ::onUpdate, ::onDelete, ::onUpdatePageSize)
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
                    StudentGroup(it, i){onDelete(it)}
					++i;
				}
            }
        }
        button { +"<<";  attrs.onClickFunction={onNavigate(state.links.first?.href)}}
        button { +"<";   attrs.onClickFunction={onNavigate(state.links.prev?.href) };attrs.disabled=state.links.prev?.href==null; }
        button { +">";   attrs.onClickFunction={onNavigate(state.links.next?.href) };attrs.disabled=state.links.next?.href==null; }
        button { +">>";  attrs.onClickFunction={onNavigate(state.links.last?.href)}}
        div {
            p {
                +"Группа  "
                input {}
            }
            button { +"Создать группу"; attrs.onClickFunction={

            } }
        }

    }

    private fun loadFromServer(pageSize: Int, call: () -> Unit = {}) {
        client.fetch(url(props.path + "?size=$pageSize")) { e ->
            val embed = JSON.parse<Embedded<JsonStudentGroups>>(e)
            val profileURL = embed._links?.profile?.href ?: ""
            client.fetch(profileURL, "application/schema+json") { s ->
                val schema = JSON.parse<Schema<*>>(s)
                val props = toArray<Property>(schema.properties)
                setState {
                    attributes = props.toList()

                    studentsGroups = embed._embedded?.studentsGroups?.toList() ?: ArrayList()
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
    private fun onDelete(item: JsonStudentGroup){
        client.delete(item._links.self?.href?:""){loadFromServer(props.pageSize)}
    }

    private fun onCreate(item: JsonStudentGroup) {
        val json = JSON.stringify(item)

        client.post(cleanUrl(state.links.self?.href),json) {
            loadFromServer(props.pageSize) {
                    val h = state.links.last?.href?:""
                    onNavigate(h)
                }
        }
    }

    private fun onUpdate(item: JsonStudentGroup) {

        var path = item._links.self?.href
        val json = JSON.stringify(item)
        /*if(path != null) {
            client.put(path, json,0) { response ->
                loadFromServer(state.pageSize)
            }
        }*/

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

fun RBuilder.InfoTableStudentGroups(path: String, pageSize: Int, gotoSubPage : ()->Unit) = child(InfoTableStudentGroups::class) {
    attrs.path = path
    attrs.gotoSubPage = gotoSubPage
    attrs.pageSize = pageSize
}



//    private fun onCreate(newEmployee: JsonStudentGroup) {
//        val json = JSON.stringify(newEmployee)
//        val h = state.links.last?.href?:""
//        client.post(cleanUrl(state.links.self?.href),json) {
//            onNavigate(h)
//        }
//    }
//
//    private fun onDelete(jsonLector: JsonStudentGroup){
//        client.delete(jsonLector._links.self?.href?:""){loadFromServer(state.pageSize)}
//    }
//
//    private fun onNavigate(url:String){
//        client.fetch(url){
//            val embed = JSON.parse<Embedded<JsonEmployees>>(it)
//
//
//            setState{
//                studentsGroups = embed._embedded?.studentsGroups?.toList()?:ArrayList()
//                links = embed._links ?: Links()
//                if(embed.page != null)
//                    page = embed.page
//            }
//        }
//    }
//
//    private fun onUpdatePageSize(pageSize: Int) {
//        if(pageSize!=state.pageSize) {
//            setState {
//                this.pageSize = pageSize
//            }
//            loadFromServer(pageSize)
//        }
//    }


//    private fun loadFromServer(pageSize: Int){
//        client.fetch(url("studentsGroups?size=$pageSize")) { e ->
//            val embed = JSON.parse<Embedded<JsonEmployees>>(e)
//            val profileURL = embed._links?.profile?.href?:""
//            if(embed.page != null)
//                state.page = embed.page
//            client.fetch(profileURL, "application/schema+json") {s->
//                val schema = JSON.parse<Schema<*>>(s)
//                val props = toArray<Property>(schema.properties)
//                setState {
//                    attributes = props.toList()
//                    studentsGroups = embed._embedded?.studentsGroups?.toList()?:ArrayList()
//                    links = embed._links ?: Links()
//                }
//            }
//        }
//    }

