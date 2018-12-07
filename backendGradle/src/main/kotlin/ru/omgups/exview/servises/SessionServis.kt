package ru.omgups.exview.servises

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.omgups.exview.jsonmodel.JsonStudentGroup
import ru.omgups.exview.model.Lector
import ru.omgups.exview.model.SessionSubject
import ru.omgups.exview.model.StudentsGroup
import ru.omgups.exview.model.Subject
import ru.omgups.exview.repo.SessionSubjectRepo

import javax.persistence.EntityManager

fun SessionSubject.setStudentGroup(studentsGroup: StudentsGroup) {
    this.studentsGroup = studentsGroup
    studentsGroup.sessionSubjects.add(this)
}

fun SessionSubject.setSubject(subject: Subject) {
    this.subject = subject
    subject.sessionSubjects.add(this)
}

fun SessionSubject.setLector(lector: Lector) {
    this.lector = lector
    lector.sessionSubjects.add(this)
}

@Service
class SessionServis {

    @Autowired
    private lateinit var sessionSubjectRepo: SessionSubjectRepo

    @Autowired
    private lateinit var em: EntityManager

    @Transactional
    fun createSessionSubjec(lectorId: Long, subjectId: Long, groupId: Long) {
        val sessionSubject = SessionSubject()
        val lector: Lector? = em.find(Lector::class.java, lectorId)
        val subject: Subject? = em.find(Subject::class.java, subjectId)
        val studentGroup: StudentsGroup? = em.find(StudentsGroup::class.java, groupId)

        if (lector != null && subject != null && studentGroup != null) {
            sessionSubject.setLector(lector)
            sessionSubject.setSubject(subject)
            sessionSubject.setStudentGroup(studentGroup)
            sessionSubjectRepo.save(sessionSubject)
            em.flush()
        }
    }
    fun refreshSessionSubjec(lectorId: Long, subjectId: Long, groupId: Long) {
        val sessionSubject = SessionSubject()
        val lector: Lector? = em.find(Lector::class.java, lectorId)
        val subject: Subject? = em.find(Subject::class.java, subjectId)
        val studentGroup: StudentsGroup? = em.find(StudentsGroup::class.java, groupId)

        if (lector != null && subject != null && studentGroup != null) {
            sessionSubject.setLector(lector)
            sessionSubject.setSubject(subject)
            sessionSubject.setStudentGroup(studentGroup)
            sessionSubjectRepo.save(sessionSubject)
            em.flush()
        }
    }
    fun deleteSessionSubjec(lectorId: Long, subjectId: Long, groupId: Long) {
        val sessionSubject = SessionSubject()
        val lector: Lector? = em.find(Lector::class.java, lectorId)
        val subject: Subject? = em.find(Subject::class.java, subjectId)
        val studentGroup: StudentsGroup? = em.find(StudentsGroup::class.java, groupId)

        if (lector != null && subject != null && studentGroup != null) {
            sessionSubject.setLector(lector)
            sessionSubject.setSubject(subject)
            sessionSubject.setStudentGroup(studentGroup)
            sessionSubjectRepo.save(sessionSubject)
            em.flush()
        }
    }
}