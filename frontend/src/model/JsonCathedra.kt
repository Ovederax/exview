package model

import rest.Href

class JsonCathedra (
        var name: String = "",
        val _links: CathedraLinks = CathedraLinks()) {
}

interface JsonCathedras {
    val cathedras: Array<JsonCathedra>
}

class CathedraLinks{
    val self: Href?=null
    val auditoriums: Href?=null
	val subjects: Href?=null
    val lectors: Href?=null
}