package ru.omgups.exview.jsonmodel

import ru.omgups.exview.jsonmodel.Href

class UpdateSessionSubject (
		var link: Href,
		var group: JsonStudentGroup = JsonStudentGroup(),
		var subject: JsonSubject = JsonSubject(),
		var lector: JsonLector = JsonLector(),
		var auditorium: JsonAuditorium = JsonAuditorium(),
		var date: Int = -1
) {

}