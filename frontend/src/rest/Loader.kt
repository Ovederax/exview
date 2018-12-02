package rest

import rest.*
import model.*


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
	
	//1.1 Получить список предметов по кафедре
	// Входной аргумент – JsonCathedra
	// Выход – List<JsonSubject> 
	fun getSubjectByCathedra(cathedra: JsonCathedra, call: (List<JsonSubject>) -> Unit) {
		val url = cathedra._links.subjects?.href
		if(url != null) {
			client.fetch(url) { e ->
				val list = JSON.parse<List<JsonSubject>>(e)
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
	
	// 2 Таблица Группы
	// Мне нужно устанавливать связки группа-предмет-преподователь для сессионных предметов.
	
	// 2.1) Получить список преподавателей по предмету
	// Входной аргумент – JsonSubject
	// Выход – List<JsonLector>
	fun getLectorsBySubject(subject: JsonSubject, call: (List<JsonLector>) -> Unit) {
		val url = subject._links.lectors?.href
		if(url != null) {
			client.fetch(url) { e ->
				val list = JSON.parse<List<JsonLector>>(e)
				call(list)
			}	
		}else {
			call(ArrayList())
		}
	}
	
	// 2.2) При отправке мной группы, предмета,
	// преподавателя нужно забить данные в таблицу LectorSubjectGroupsRepo
	// Mapping  «/createSessionSubject »
	// Входной аргумент – PairGroupSubjectLector
	fun createSessionSubject(pair: PairGroupSubjectLector, call: () -> Unit = {}) {
		val url = url("createSessionSubject")
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




















