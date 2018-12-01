package comp.items

import comp.updDialogs.SessionSubjectAddDialog
import kotlinx.html.js.onClickFunction
import model.JsonStudentGroup
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.td
import react.dom.tr

class StudentGroup: RComponent<StudentGroup.Props, RState>(){
    interface Props: RProps {
        var jsonStudentGroup: JsonStudentGroup
        var key:String
        var onDelete:()->Unit
		var num: Int
    }
    override fun RBuilder.render() {
        tr {
            td { +props.num.toString() }
            td { +props.jsonStudentGroup.name }
            td {
//                button {
//                    attrs { onClickFunction = {
//                            +props.jsonStudentGroup._links.subjects!!.href
//                        }}
//                    +"Просмотр"
//                }
                SessionSubjectAddDialog(props.jsonStudentGroup, props.num)
            }

            td {
                button {
                    attrs { onClickFunction = {
                        props.onDelete()
                    } }
                    +"Удалить"
                }
            }
        }
    }
}
fun RBuilder.StudentGroup(jsonStudentGroup: JsonStudentGroup, num: Int, onDelete:()->Unit = {})
        = child(StudentGroup::class) {
    attrs.jsonStudentGroup = jsonStudentGroup
    attrs.onDelete = onDelete
    attrs.num = num
}
