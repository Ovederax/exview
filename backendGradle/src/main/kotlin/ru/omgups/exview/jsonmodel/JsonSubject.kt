package ru.omgups.exview.jsonmodel


class SubjectLinks{
    val self: Href?=null
	val subject: Href?=null
	val lectors: Href?=null
    val studentGroup: Href?=null
}

class JsonSubject (
        var name: String = "",
        val _links: SubjectLinks = SubjectLinks()) {
}

interface JsonSubjects {
    val subjects: Array<JsonSubject>

}