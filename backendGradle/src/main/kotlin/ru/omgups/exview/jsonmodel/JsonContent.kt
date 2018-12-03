package ru.omgups.exview.jsonmodel

class JsonContent (
        var name: String = "",
        val _links: Links = Links()) {

    /*companion object {
        fun fromPropsArray(auditorium: Array<String>): JsonLector =
                JsonLector(lectors[0])
    }*/
}
interface JsonContents{
    val contents:Array<JsonContent>
}