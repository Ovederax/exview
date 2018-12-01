package model

import rest.Href

class JsonSessionSubject () {
	var _links: SessionSubjectLinks = SessionSubjectLinks()
}

interface JsonSessionSubjects {
    val sessionSubjects: Array<JsonSessionSubject>
}

class SessionSubjectLinks {
	var self: Href? = null
	var sessionSubject: Href? = null
	var studentGroup: Href? = null
	var subject: Href? = null
	var lector: Href? = null
}