package comp.items

import model.JsonAuditorium
import model.JsonSessionSubject
import model.PojoSessionSubject
import react.*
import react.dom.*
import rest.Loader


class ExamenByAuditoriumEdit: RComponent<ExamenByAuditoriumEdit.Props, ExamenByAuditoriumEdit.State>(){
    interface Props: RProps {
        var jsonAuditorium: JsonAuditorium
        var num: Int
//        var onDelete:()->Unit
//        var cathedras: List<JsonCathedra>
//        var allSubjects: List<JsonSubject>
    }

    interface State: RState {
        var sessionSubjects : List<JsonSessionSubject>
    }

    init {
        state.sessionSubjects = ArrayList()
    }

    fun reloadData() {
        Loader().getSessionSubjectByAuditorium(props.jsonAuditorium) { it ->
            state.sessionSubjects = it
            setState{}
        }
    }

    override fun componentDidMount() {
        reloadData()
    }


    override fun RBuilder.render() {
        tr {
            td { +props.jsonAuditorium.name }
            for (i in 0..30) {
                td {
                    div {
                        val day = i+1
                        val id = props.num*100+i
                        a("#editByAuditoruum_" + id.toString()) {
                            +"+"
                        }
                        div("modalDialogSessionSubject") {
                            attrs["id"] = "editByAuditoruum_" + id.toString()
                            div {
                                a("#", classes = "close") { +"X" }
                                h2 { +"Назначение сессионных предметов для ауд. ${props.jsonAuditorium.name}" }

                                /*table { tbody {
                                    tr {
                                        td {
                                            +"Предмет "
                                        }
                                        td {
                                            select(classes = "selectClasses") {
                                                attrs["ref"] = "selectSubject"
                                                attrs.onChangeFunction = {
                                                    val select = findDOMNode(refs["selectSubject"]) as HTMLSelectElement
                                                    val id = select.selectedIndex
                                                    if(id == 0) {
                                                        state.selectedSubject = null
                                                    } else {
                                                        state.selectedSubject = state.subjectsToSelect[id-1]
                                                    }
                                                    (findDOMNode(refs["selectLector"]) as HTMLSelectElement).selectedIndex = 0
                                                    setState {}
                                                }
                                                option {
                                                    +"(пусто)"
                                                }
                                                state.subjectsToSelect.map { prop ->
                                                    var ch = true
                                                    for (it in state.sessionSubjects) {
                                                        var item = state.pojoSessionSubjects.get(it)
                                                        if(item != null)
                                                            if(item.subjectName == prop.name) {
                                                                ch = false
                                                            }
                                                    }
                                                    if(ch)
                                                        option {
                                                            +prop.name
                                                        }
                                                }
                                            }
                                        }
                                        td {
                                            attrs["rowSpan"] = "2"
                                            button {
                                                +"Создать"
                                                attrs.onClickFunction= { e->
                                                    e.preventDefault()
                                                    var selectSubject = findDOMNode(refs["selectSubject"]) as HTMLSelectElement
                                                    val idSubj = selectSubject.selectedIndex
                                                    var selectLector = findDOMNode(refs["selectLector"]) as HTMLSelectElement
                                                    val idLect = selectLector.selectedIndex

                                                    if(idSubj!=0 && idLect!=0) {
                                                        var lectorList = props.lectorsMap.get(state.subjectsToSelect[idSubj-1])
                                                        if(lectorList != null) {
                                                            var pair = PairGroupSubjectLector()
                                                            pair.group = props.group
                                                            pair.subject = state.subjectsToSelect[idSubj-1]
                                                            pair.lector = lectorList[idLect-1]
                                                            Loader().createSessionSubject(pair) {
                                                                selectSubject.selectedIndex = 0
                                                                selectLector.selectedIndex = 0
                                                                reload()
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    tr {
                                        td { +"Лектор" }
                                        td {
                                            select(classes = "selectClasses") {
                                                attrs["ref"] = "selectLector"
                                                option {
                                                    +"(пусто)"
                                                }
                                                val sel = state.selectedSubject
                                                if(sel != null) {
                                                    val lectors = props.lectorsMap.get(sel)
                                                    if(lectors != null) {
                                                        lectors.map { prop ->
                                                            option {
                                                                +prop.name
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } }
                                if(state.sessionSubjects.size>0) {
                                    br{ }
                                    br{ }
                                    table { tbody {
                                        tr {
                                            th { +"Предмет"}
                                            th { +"Преподователь"}
                                            th { +"Дата сдачи"}
                                            th { +"Аудитория"}
                                            th { +"Удалить"}
                                        }
                                        for (it in state.sessionSubjects) {
                                            var item = state.pojoSessionSubjects.get(it)
                                            if(item != null) {
                                                tr {
                                                    td {+item.subjectName}
                                                    td {+item.lectorName}
                                                    if(item.auditorium != "") td {+item.auditorium} else td {+"-"}
                                                    if(item.date != -1) td {+item.date.toString()} else td {+"-"}
                                                    td {button {
                                                        +"Удалить"
                                                    }}
                                                }
                                            }
                                        }
                                    }}
                                    br{ }
                                    br{ }
                                }
*/
                            }
                        }
                    }
                }
            }

//            td { +props.jsonLector.name }
//            td {
//                UpdateDialogCathedras(props.num, props.cathedras, state.cathedra) {
//                    //Установка кафедры лектору
//                    Loader().setCathedraInLector(PairLectorCathedra(props. jsonLector, it)) {
//                        reloadData()
//                    }
//                }
//            }
//            td {
//                //props.jsonLector._links.subjects?.href,
//                UpdateDialogSubjects(props.jsonLector,props.num, props.allSubjects) { it ->
//                    //добавление предметов лектору
//                    val pair = PairLectorSubjects()
//                    pair.lector = props.jsonLector
//                    pair.subjects = it
//                    Loader().setSubjectsInLector(pair) {
//                        reloadData()
//                    }
//                }
//            }
//            td {
//                button {
//                    attrs { onClickFunction = {
//                        props.onDelete()
//                    } }
//                    +"Удалить"
//                }
//            }
        }
    }
}

fun RBuilder.ExamenByAuditoriumEdit(jsonAuditorium: JsonAuditorium, num: Int, sessionSubjects: List<JsonSessionSubject>, pojoSessionSubjects: MutableMap<JsonSessionSubject, PojoSessionSubject>) = child(ExamenByAuditoriumEdit::class) {
    attrs.jsonAuditorium = jsonAuditorium
    attrs.num = num
}
