package model

import rest.Links

class JsonAuditorium (
        val num: Long = 0,
        val _links: Links = Links()) {

    /*companion object {
        fun fromPropsArray(auditorium: Array<String>): JsonLector =
                JsonLector(lectors[0])
    }*/
}