package ru.omgups.exview.jsonmodel

data class PairLectorCathedra (
	var lector: JsonLector = JsonLector(),
	var cathedra: JsonCathedra? = JsonCathedra()
) {

}