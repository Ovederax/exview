package ru.omgups.exview.jsonmodel

class JsonCathedra (
        var name: String = "",
        val _links: CathedraLinks = CathedraLinks()) {
}

interface JsonCathedras {
    val cathedras: Array<JsonCathedra>
}

class CathedraLinks{
    val self: Href?=null
    val auditorium: Href?=null
	val subjects: Href?=null
	val facultets: Href?=null
    val lectors: Href?=null
}