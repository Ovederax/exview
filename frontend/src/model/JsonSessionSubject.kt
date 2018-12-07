package model

import rest.Href

class JsonSessionSubject () {
	var date: Int = -1
	var _links: SessionSubjectLinks = SessionSubjectLinks()
}

interface JsonSessionSubjects {
    val sessionSubjects: Array<JsonSessionSubject>
}

class SessionSubjectLinks {
	var self: Href? = null
	var sessionSubject: Href? = null
	var studentsGroup: Href? = null
	var subject: Href? = null
	var lector: Href? = null
}

class PojoSessionSubject (
	var groupName: String ="",
	var subjectName: String ="",
	var lectorName: String ="",
	var auditorium: String ="",
	var date: Int = -1
) {}