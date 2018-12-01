package model

import rest.Href


class JsonStudentGroup (
        var name: String = "",
        val _links: StudentGroupLinks = StudentGroupLinks()) {
}


interface JsonStudentGroups {
    val studentsGroups: Array<JsonStudentGroup>
}

class StudentGroupLinks{
    val self: Href?=null
    val auditorium: Href?=null
	val subjects: Href?=null
	val facultets: Href?=null
    val lectors: Href?=null
}