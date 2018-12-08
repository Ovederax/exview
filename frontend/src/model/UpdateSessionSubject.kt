package model

import rest.*

class UpdateSessionSubject (
	var link: Href,
	var group: JsonStudentGroup = JsonStudentGroup(),
	var subject: JsonSubject = JsonSubject(),
	var lector: JsonLector = JsonLector(),
	var auditorium: JsonAuditorium = JsonAuditorium(),
	var date: Int = -1
) {

}