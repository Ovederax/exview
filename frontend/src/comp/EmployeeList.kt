package comp

//class EmployeeList: RComponent<EmployeeList.Props,RState>() {
//    var refs:dynamic = null
//
//    interface Props:RProps{
//        var lectors: List<JsonLector>
//        var onNavigate: (String)->Unit
//        var onDelete: (JsonLector)->Unit
//        var onUpdatePageSize: (Int)->Unit
//        var links: Links
//        var pageSize:Int
//        var page: Page
//    }
//
//    override fun RBuilder.render() {
//        input {
//            attrs["ref"] = "pageSize"
//            attrs["defaultValue"] = props.pageSize
////            attrs { onClickFunction = ::handleInput }
//            attrs { onBlurFunction = ::handleInput }
//        }
//        table {
//            tbody {
//                tr {
//					th { +"Number"}
//                    th { +"First Name" }
//                    th { +"Last Name" }
//                    th { +"Description" }
//                    th { +"delete" }
//                }
//                var i = 1
//                if(props.page != null) {
//                    var a = props.page.number
//                    var b = props.page.size
//                    if(a != null && b!=null) {
//                        i += a*b
//                    }
//                }
//
//                props.lectors.map {
//                    Lector(it, i, it._links.self?.href ?: ""){props.onDelete(it)}
//					++i;
//				}
//            }
//        }
//        val l = props.links
//        arrayOf("<<" to l.first,
//                "<" to l.prev,
//                ">" to l.next,
//                ">>" to l.last)
//                .map{(str, link)->
//                    if(link!=null)
//                        button {
//                            attrs{onClickFunction = {handleNav(it, link.href)}}
//                            +str
//                        }
//                }
//    }
//
//    private fun handleNav(e: Event, href:String){
//        e.preventDefault()
//        props.onNavigate(href)
//    }
//
//    private fun handleInput(e: Event) {
//        e.preventDefault()
//        val input = findDOMNode(this.refs["pageSize"]) as HTMLInputElement
//        val ps = input.value.toInt()
//        if("^[0-9]+$".toRegex().matches(input.value))
//            props.onUpdatePageSize(ps)
//        else
//            input.value = props.pageSize.toString()
//    }
//}
//
//fun RBuilder.EmployeeList(lectors: List<JsonLector>,
//                          links:Links,
//                          page: Page,
//                          pageSize:Int,
//                          onNavigate: (String)->Unit,
//                          onDelete: (JsonLector)->Unit,
//                          onUpdatePageSize: (Int)->Unit)
//        = child(EmployeeList::class) {
//    attrs.page = page
//    attrs.lectors = lectors
//    attrs.links = links
//    attrs.pageSize = pageSize
//    attrs.onNavigate = onNavigate
//    attrs.onDelete = onDelete
//    attrs.onUpdatePageSize = onUpdatePageSize
//}
