package rest

import comp.items.SessionSubject
import rest.*
import model.*

// добавить!!!
// /refreshSessionSubject

/**
	Общее строение
	Внутренние объекты будут иметь строение данные + ссылки ->
	можно взять классы из model frontenda.
	Эти объекты будут помещены в обрамляющий объект
	если нужно передать 2 и более аргумента.
*/
class Loader {
	val client = Client()
	private fun url(str: String) =
        "http://localhost:8080/$str"
		
	// 1 Для таблицы лекторов
	// загрузка списка доступных кафедр
	// http://localhost:8080/cathedras
    fun loadCathedrasList(url: String, call: (List<JsonCathedra>) -> Unit) {
		client.fetch(url) { e ->
            val embed = JSON.parse<Embedded<JsonCathedras>>(e)
			val cathedras = embed._embedded?.cathedras?.toList() ?: ArrayList()
			call(cathedras)
        }  
    }
	
	fun loadLectorsList(call: (List<JsonLector>) -> Unit) {
		val url: String = url("lectors")
		client.fetch(url) { e ->
            val embed = JSON.parse<Embedded<JsonLectors>>(e)
			val lectors = embed._embedded?.lectors?.toList() ?: ArrayList()
			call(lectors)
        }  
    }

	fun loadAuditoriumList(call: (List<JsonAuditorium>) -> Unit) {
		val url: String = url("auditoriums")
		client.fetch(url) { e ->
			val embed = JSON.parse<Embedded<JsonAuditoriums>>(e)
			val auditoriums = embed._embedded?.auditoriums?.toList() ?: ArrayList()
			call(auditoriums)
		}
	}

	fun loadGroupList(call: (List<JsonStudentGroup>) -> Unit) {
		val url: String = url("studentsGroups")
		client.fetch(url) { e ->
			val embed = JSON.parse<Embedded<JsonStudentGroups>>(e)
			val groups = embed._embedded?.studentsGroups?.toList() ?: ArrayList()
			call(groups)
		}
	}
	//1.1 Получить список предметов по кафедре
	// Входной аргумент – JsonCathedra
	// Выход – List<JsonSubject> 
	fun getSubjectByCathedra(cathedra: JsonCathedra, call: (List<JsonSubject>) -> Unit) {
		val url = cathedra._links.subjects?.href
		if(url != null) {
			client.fetch(url) { e ->
				val embed = JSON.parse<Embedded<JsonSubjects>>(e)
				val list = embed._embedded?.subjects?.toList() ?: ArrayList()
				call(list)
			} 
		} else {
			call(ArrayList())
		}
	}
	
	fun getLectorsByCathedra(cathedra: JsonCathedra, call: (List<JsonLector>) -> Unit) {
		val url = cathedra._links.lectors?.href
		if(url != null) {
			client.fetch(url) { e ->
				val embed = JSON.parse<Embedded<JsonLectors>>(e)
				val list = embed._embedded?.lectors?.toList() ?: ArrayList()
				call(list)
			} 
		} else {
			call(ArrayList())
		}
	}
	
	fun getAuditoriumsByCathedra(cathedra: JsonCathedra, call: (List<JsonAuditorium>) -> Unit) {
		val url = cathedra._links.auditoriums?.href
		if(url != null) {
			client.fetch(url) { e ->
				val embed = JSON.parse<Embedded<JsonAuditoriums>>(e)
				val list = embed._embedded?.auditoriums?.toList() ?: ArrayList()
				call(list)
			} 
		} else {
			call(ArrayList())
		}
	}
	
	fun getLectorsBySubject(subject: JsonSubject, call: (List<JsonLector>) -> Unit) {
		val url = subject._links.lectors?.href
		if(url != null) {
			client.fetch(url) { e ->
				val embed = JSON.parse<Embedded<JsonLectors>>(e)
				val list = embed._embedded?.lectors?.toList() ?: ArrayList()
				call(list)
			} 
		} else {
			call(ArrayList())
		}
	}
	
	fun loadAllSubjects(call: (List<JsonSubject>) -> Unit) {
		val url = url("subjects")
		if(url != null) {
			client.fetch(url) { e ->
				val embed = JSON.parse<Embedded<JsonSubjects>>(e)
				val list = embed._embedded?.subjects?.toList() ?: ArrayList()
				call(list)
			} 
		} else {
			call(ArrayList())
		}
	}
	
	fun getLectorSubjects(lector: JsonLector, call: (List<JsonSubject>) -> Unit) {
		val url = lector._links.subjects?.href
		if(url != null) {
			client.fetch(url) { e ->
				val embed = JSON.parse<Embedded<JsonSubjects>>(e)
				val list = embed._embedded?.subjects?.toList() ?: ArrayList()
				call(list)
			} 
		} else {
			call(ArrayList())
		}
	}
	
	// 1.2 Установить Кафедру для преподавателя
	// Mapping  «/setCathedraInLector »
	// Входной аргумент – PairLectorCathedra
	// Выход – нет 
	fun setCathedraInLector(pair: PairLectorCathedra, call: () -> Unit = {}) {
		val url = url("setCathedraInLector")
		client.post(url, JSON.stringify(pair)) { e ->
			call()
        }
	}
	
	// 1.3 Установить Список из предметов в Преподавателя
	// Mapping  «/setSubjectsInLector »
	// Входной аргумент – PairLectorSubjects
	// Выход – нет 
	fun setSubjectsInLector(pair: PairLectorSubjects, call: () -> Unit = {}) {
		val url = url("setSubjectsInLector")
		client.post(url, JSON.stringify(pair)) { e ->
			call()
        }
	}
	
	//1.4 Получить кафедру преподователя
	// Входной аргумент – JsonLector
	// Выход – нет 
	fun getCathedraByLector(lector: JsonLector, call: (JsonCathedra?) -> Unit = {}) {
		val url = lector._links.cathedra?.href
		if(url != null) {
			client.fetch(url) { e ->
				if(e == "") {
					call(null)
				} else {
					call(JSON.parse<JsonCathedra>(e))
				}
			}
		}else {
			call(null)
		}

	}
	
	// 2 Таблица Группы

	fun getAllSessionSubject(call: (List<JsonSessionSubject>) -> Unit) {
		val url = url("sessionSubjects")
		client.fetch(url) { e ->
			val embed = JSON.parse<Embedded<JsonSessionSubjects>>(e)
			val list = embed._embedded?.sessionSubjects?.toList() ?: ArrayList()
			call(list)
		} 
	}
	
	fun getSessionSubjectByGroup(group: JsonStudentGroup, call: (List<JsonSessionSubject>) -> Unit) {
		val url = group._links.sessionSubjects?.href
		if(url != null) {
			client.fetch(url) { e ->
				val embed = JSON.parse<Embedded<JsonSessionSubjects>>(e)
				val list = embed._embedded?.sessionSubjects?.toList() ?: ArrayList()
				call(list)
			} 
		} else {
			call(ArrayList())
		}
	}

	fun getSessionSubjectByAuditorium(jsonAuditorium: JsonAuditorium, call: (List<JsonSessionSubject>) -> Unit) {
		val url = jsonAuditorium._links.sessionSubjects?.href
		if(url != null) {
			client.fetch(url) { e ->
				val embed = JSON.parse<Embedded<JsonSessionSubjects>>(e)
				val list = embed._embedded?.sessionSubjects?.toList() ?: ArrayList()
				call(list)
			}
		} else {
			call(ArrayList())
		}
	}
	
	fun getPojoSessionSubject(subject: JsonSessionSubject, call: (PojoSessionSubject) -> Unit) {
		var counter = 0
		val groupName: String = ""
		val subjectName: String = ""
		val lectorName: String = ""
		val auditorium: String = ""
		val date: Int = subject.date
		val pojo = PojoSessionSubject(groupName, subjectName, lectorName, auditorium, date)
		
		var url = subject._links.studentsGroup?.href
		if(url != null) {
			Client().fetch(url) { e ->
				val item = JSON.parse<JsonStudentGroup>(e)
				++counter
				pojo.groupName = item.name
				if(counter == 4) {
					call(pojo)
				}
			}
		} else {
			++counter
		}
		
		url = subject._links.auditorium?.href
		if(url != null) {
			Client().fetch(url) { e ->
				if(e != "") {
					val item = JSON.parse<JsonAuditorium>(e)
					pojo.auditorium = item.name
				}
				++counter
				if(counter == 4) {
					call(pojo)
				}
			}
		} else {
			++counter
		}
		
		url = subject._links.subject?.href
		if(url != null) {
			Client().fetch(url) { e ->
				if(e != "") {
					val item = JSON.parse<JsonSubject>(e)
					pojo.subjectName = item.name
				}
				++counter
				if(counter == 4) {
					call(pojo)
				}
			}
		} else {
			++counter
		}	
		
		url = subject._links.lector?.href
		if(url != null) {
			Client().fetch(url) { e ->
				val item = JSON.parse<JsonLector>(e)
				pojo.lectorName = item.name
				++counter
				if(counter == 4) {
					call(pojo)
				}
			}
		} else {
			++counter
			if(counter == 4) {
				call(pojo)
			}
		}	
	}
	
	fun getUpdateSessionSubject(subject: JsonSessionSubject, call: (UpdateSessionSubject) -> Unit) {
		var counter = 0
		
		val link = subject._links.self ?: Href()
		val pojo = UpdateSessionSubject(link)
		pojo.date = subject.date
		
		var url = subject._links.studentsGroup?.href
		if(url != null) {
			Client().fetch(url) { e ->
				val item = JSON.parse<JsonStudentGroup>(e)
				++counter
				pojo.group = item
				if(counter == 4) {
					call(pojo)
				}
			}
		} else {
			++counter
		}
		
		url = subject._links.auditorium?.href
		if(url != null) {
			Client().fetch(url) { e ->
				if(e != "") {
					val item = JSON.parse<JsonAuditorium>(e)
					pojo.auditorium = item
				}
				++counter
				if(counter == 4) {
					call(pojo)
				}
			}
		} else {
			++counter
		}
		
		url = subject._links.subject?.href
		if(url != null) {
			Client().fetch(url) { e ->
				if(e != "") {
					val item = JSON.parse<JsonSubject>(e)
					pojo.subject = item
				}
				++counter
				if(counter == 4) {
					call(pojo)
				}
			}
		} else {
			++counter
		}	
		
		url = subject._links.lector?.href
		if(url != null) {
			Client().fetch(url) { e ->
				val item = JSON.parse<JsonLector>(e)
				pojo.lector = item
				++counter
				if(counter == 4) {
					call(pojo)
				}
			}
		} else {
			++counter
			if(counter == 4) {
				call(pojo)
			}
		}	
	}

	fun createSessionSubject(pair: PairGroupSubjectLector, call: () -> Unit = {}) {
		val url = url("createSessionSubject")
		client.post(url, JSON.stringify(pair)) { e ->
			call()
        }
	}
	
	fun refreshSessionSubject(pair: UpdateSessionSubject, call: () -> Unit = {}) {
		val url = url("refreshSessionSubject")
		client.post(url, JSON.stringify(pair)) { e ->
			call()
        }
	}
	
	fun deleteSessionSubject(pair: UpdateSessionSubject, call: () -> Unit = {}) {
		val url = url("deleteSessionSubject")
		client.post(url, JSON.stringify(pair)) { e ->
			call()
        }
	}
	
	fun freeSessionSubject(pair: UpdateSessionSubject, call: () -> Unit = {}) {
		val url = url("freeSessionSubject")
		client.post(url, JSON.stringify(pair)) { e ->
			call()
        }
	}
	// 2.3) Получить список уже настроенных пар Предмет-Преподаватель для группы
	// Входной аргумент – JsonStudentGroup
	// Выход – List<PairLectorSubject> 	
	fun getSubjectAndLectorByStudentGroup(studentGroup: JsonStudentGroup, call: (List<PairLectorSubject>)->Unit) {
		val url = url("getSubjectAndLectorByStudentGroup")
		client.fetch(url) { e ->
            val list = JSON.parse<List<PairLectorSubject>>(e)
			call(list)
        } 
	}
}




















