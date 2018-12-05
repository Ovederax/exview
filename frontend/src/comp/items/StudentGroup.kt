package comp.items

import comp.updDialogs.SessionSubjectAddDialog
import kotlinx.html.js.onClickFunction
import model.*
import react.*
import rest.*
import react.dom.*

class StudentGroup: RComponent<StudentGroup.Props, StudentGroup.State>(){
    interface Props: RProps {
        var jsonStudentGroup: JsonStudentGroup
        var key:String
        var onDelete:()->Unit
		var num: Int
		var subjects: List<JsonSubject>
        var lectorsMap: MutableMap<JsonSubject, List<JsonLector>>
    }
	
	interface State: RState {
		//var sessionSubjects: List <SessionSubject> 
	}
	
    override fun RBuilder.render() {
        tr {
            td { +props.num.toString() }
            td { +props.jsonStudentGroup.name }
            td {
                SessionSubjectAddDialog(props.jsonStudentGroup, props.num, props.subjects, props.lectorsMap )
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
fun RBuilder.StudentGroup(jsonStudentGroup: JsonStudentGroup, num: Int, subjects: List<JsonSubject>, lectorsMap: MutableMap<JsonSubject, List<JsonLector>>,  onDelete:()->Unit = {})
        = child(StudentGroup::class) {
    attrs.jsonStudentGroup = jsonStudentGroup
    attrs.onDelete = onDelete
    attrs.num = num
    attrs.subjects = subjects
    attrs.lectorsMap = lectorsMap
}
