package model

import rest.Href


class JsonStudentGroup (
        var name: String = "",
        val _links: StudentGroupLinks = StudentGroupLinks()) {
}


interface JsonStudentGroups {
    val studentsGroups: Array<JsonStudentGroup>
}

class StudentGroupLinks {
    val self: Href?=null
    val studentsGroup: Href?=null
    val sessionSubjects: Href?=null

}