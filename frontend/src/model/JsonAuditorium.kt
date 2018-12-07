package model

import rest.Href
import rest.Links

class JsonAuditorium (
        val name: String = "",
        val _links: AuditoriumLinks = AuditoriumLinks()) {
}

interface JsonAuditoriums {
    val auditoriums: Array<JsonAuditorium>
}

class AuditoriumLinks{
    val self: Href?=null
    //val auditoriums: Href?=null
    val sessionSubjects: Href?=null
    //val lectors: Href?=null
}