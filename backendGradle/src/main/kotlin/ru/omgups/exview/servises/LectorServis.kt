package ru.omgups.exview.servises

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.omgups.exview.model.Cathedra
import ru.omgups.exview.model.Lector
import ru.omgups.exview.model.Subject
import javax.persistence.EntityManager

fun Lector.addSubject(subj: Subject) {
    subjects.add(subj)
    subj.lectors.add(this)
}

fun Lector.setCathedra(cathedra: Cathedra?) {
    this.cathedra = cathedra
    cathedra?.lectors?.add(this)
}

@Service
class LectorServis {
    @Autowired
    private lateinit var em: EntityManager

    @Transactional
    fun setCathedraToLector(cathedraId: Long, lectorId: Long) {
        var cathedra: Cathedra? = null
        if(cathedraId != -1L) {
            cathedra = em.find(Cathedra::class.java, cathedraId)
        }
        val lector: Lector? = em.find(Lector::class.java, lectorId)
        val cathedraNow: Cathedra?
        if (/*cathedra!=null &&*/lector != null) {
            if (lector.cathedra?.id != null) {
                cathedraNow = em.find(Cathedra::class.java, lector.cathedra?.id)
                cathedraNow?.lectors?.remove(lector)
                lector.cathedra = null
                em.flush()
            }
            lector.setCathedra(cathedra)
        }
        em.flush()
    }

    @Transactional
    fun setSubjectsToLector(lectorId: Long, subjects: MutableSet<Long>) {
        val lector: Lector? = em.find(Lector::class.java, lectorId)
        lector ?: return
        val listToRemove = ArrayList(lector.subjects)
        for (item in listToRemove) {
            item.lectors.remove(lector)
            lector.subjects.remove(item)
        }
        em.flush()
        for (id in subjects) {
            val sub: Subject? = em.find(Subject::class.java, id)
            if (sub != null) {
                lector.addSubject(sub)
            }
        }
        em.flush()
    }
}


//    @Autowired
//    private lateinit var lectorRepo: LectorRepo
//    @Autowired
//    private lateinit var subjectRepo: SubjectRepo
//    @Autowired
//    private lateinit var cathedraRepo: CathedraRepo
