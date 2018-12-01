package model

import rest.Href

class LectorLinks{
    val self: Href?=null
    val lector: Href?=null
    val cathedra: Href?=null
    val subjects: Href?=null

}
class JsonLector (
    val name: String = "",
    val _links: LectorLinks = LectorLinks()) {

    companion object {
        fun fromPropsArray(employee: Array<String>): JsonLector =
                JsonLector(employee[0])
    }
}
class PojoLector {
	var name : String = ""
	//var cathedrs: Set<> = HashSet()
	//var subjects: Set = HashSet()
}

interface JsonLectors {
    val lectors: Array<JsonLector>
}


