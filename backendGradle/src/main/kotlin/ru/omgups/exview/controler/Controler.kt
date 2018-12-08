package ru.omgups.exview.controler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.omgups.exview.servises.LectorServis
import ru.omgups.exview.servises.SessionServis
import ru.omgups.exview.jsonmodel.PairGroupSubjectLector
import ru.omgups.exview.jsonmodel.PairLectorCathedra
import ru.omgups.exview.jsonmodel.PairLectorSubjects
import ru.omgups.exview.jsonmodel.UpdateSessionSubject

@RestController
class Controler {
    @Autowired
    lateinit var lectorServises: LectorServis
    @Autowired
    lateinit var sessionServis: SessionServis

    @RequestMapping(value = ["/setCathedraInLector"],
            method = [(RequestMethod.POST)],
            headers = ["Accept=application/json"])
    fun setCathedraToLector(@RequestBody lectorAndCathedra: PairLectorCathedra) {
//        println(lectorAndCathedra.toString())
//        println(lectorAndCathedra.lector._links.self?.href)
//        println()
//        println(lectorAndCathedra.cathedra?._links?.self?.href)

        var lectorId = lectorAndCathedra.lector._links.self?.href?.substringAfterLast("/")?.toLong()
        var cathedraId = lectorAndCathedra.cathedra?._links?.self?.href?.substringAfterLast("/")?.toLong()
        if(cathedraId == null) {
            cathedraId = -1; // удаление кафедры из Lectora
        }
        if (lectorId != null /*&& cathedraId != null*/) {
            //println("lectorId = $lectorId cathedra = $cathedraId")
            lectorServises.setCathedraToLector(cathedraId, lectorId)
        }
    }

    @RequestMapping(value = ["/setSubjectsInLector"],
            method = [(RequestMethod.POST)],
            headers = ["Accept=application/json"])
    fun setSubjectsToLector(@RequestBody pairLectorSubjects: PairLectorSubjects) {
        var lectorId: Long? = pairLectorSubjects.lector._links.self?.href?.substringAfterLast("/")?.toLong() ?: return
        val subjects: MutableSet<Long> = HashSet()
        /*if (pairLectorSubjects.subjects.isEmpty()) { // может быть пустым - тогда поудалять все предъыдущии предметы
            return
        }*/
        for (sub in pairLectorSubjects.subjects) {
            sub._links.self?.href?.substringAfterLast("/")?.toLong()?.let { subjects.add(it) }
        }
        lectorServises.setSubjectsToLector(lectorId!!, subjects)
    }

    @RequestMapping(value = ["/createSessionSubject"],
            method = [(RequestMethod.POST)],
            headers = ["Accept=application/json"])
    fun createSessionSubjec(@RequestBody item: PairGroupSubjectLector) {
        val lectorId = item.lector._links.self?.href?.substringAfterLast("/")?.toLong()
        val subjectId = item.subject._links.self?.href?.substringAfterLast("/")?.toLong()
        val groupId = item.group._links.self?.href?.substringAfterLast("/")?.toLong()
        if (lectorId != null && groupId != null && subjectId != null) {
            sessionServis.createSessionSubjec(lectorId, subjectId, groupId)
        }
    }
    @RequestMapping(value = ["/refreshSessionSubject"],
            method = [(RequestMethod.POST)],
            headers = ["Accept=application/json"])
    fun refreshSessionSubject(@RequestBody item: UpdateSessionSubject) {
        val id  = item.link.href.substringAfterLast("/").toLong()
        val lectorId = item.lector._links.self?.href?.substringAfterLast("/")?.toLong()
        val subjectId = item.subject._links.self?.href?.substringAfterLast("/")?.toLong()
        val groupId = item.group._links.self?.href?.substringAfterLast("/")?.toLong()
        val auditoriumId = item.auditorium._links.self?.href?.substringAfterLast("/")?.toLong()
        if (lectorId != null && groupId != null && subjectId != null && auditoriumId != null) {
            sessionServis.refreshSessionSubjec(id, lectorId, subjectId, groupId, auditoriumId, item.date)
        }
    }

    @RequestMapping(value = ["/freeSessionSubject"],
            method = [(RequestMethod.POST)],
            headers = ["Accept=application/json"])
    fun freeSessionSubject(@RequestBody item: UpdateSessionSubject) {
        val id  = item.link.href.substringAfterLast("/").toLong()
        sessionServis.freeSessionSubjec(id)
    }

    @RequestMapping(value = ["/deleteSessionSubject"],
            method = [(RequestMethod.POST)],
            headers = ["Accept=application/json"])
    fun deleteSessionSubject(@RequestBody item: UpdateSessionSubject) {
        val id  = item.link.href.substringAfterLast("/").toLong()
        sessionServis.deleteSessionSubjec(id)
    }

}