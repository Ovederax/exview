package ru.omgups.exview.servises

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.omgups.exview.jsonmodel.JsonStudentGroup
import ru.omgups.exview.model.*
import ru.omgups.exview.repo.SessionSubjectRepo

import javax.persistence.EntityManager

fun SessionSubject.setStudentGroup(studentsGroup: StudentsGroup?) {
    this.studentsGroup = studentsGroup
    studentsGroup?.sessionSubjects?.add(this)
}

fun SessionSubject.setSubject(subject: Subject?) {
    this.subject = subject
    subject?.sessionSubjects?.add(this)
}

fun SessionSubject.setLector(lector: Lector?) {
    this.lector = lector
    lector?.sessionSubjects?.add(this)
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

    @Transactional
    fun refreshSessionSubjec(id: Long, lectorId: Long, subjectId: Long, groupId: Long, audId: Long, date: Int) {
        val sessionSubject: SessionSubject? = em.find(SessionSubject::class.java, id)
        val lector: Lector? = em.find(Lector::class.java, lectorId)
        val subject: Subject? = em.find(Subject::class.java, subjectId)
        val studentGroup: StudentsGroup? = em.find(StudentsGroup::class.java, groupId)
        val auditorium: Auditorium? = em.find(Auditorium::class.java, audId)
        if (sessionSubject != null) {
            val session:SessionSubject = sessionSubject
            if(session.auditorium != auditorium) {
                session.auditorium?.sessionSubjects?.remove(session)
                session.auditorium = auditorium
                auditorium?.sessionSubjects?.add(session)
            }
            if(session.studentsGroup != studentGroup) {
                session.studentsGroup?.sessionSubjects?.remove(session)
                session.setStudentGroup(studentGroup)
            }
            if(session.lector != lector) {
                session.lector?.sessionSubjects?.remove(session)
                session.setLector(lector)
            }
            if(session.subject != subject) {
                session.subject?.sessionSubjects?.remove(session)
                session.setSubject(subject)
            }
            session.date = date
            sessionSubjectRepo.save(session)
            em.flush()
        }
    }

    @Transactional
    fun deleteSessionSubjec(id: Long) {
        val sessionSubject: SessionSubject? = em.find(SessionSubject::class.java, id)

        if (sessionSubject != null) {
            val session:SessionSubject = sessionSubject
            session.auditorium?.sessionSubjects?.remove(session)
            session.studentsGroup?.sessionSubjects?.remove(session)
            session.lector?.sessionSubjects?.remove(session)
            session.subject?.sessionSubjects?.remove(session)

            session.auditorium = null
            session.studentsGroup = null
            session.lector = null
            session.subject = null
            sessionSubjectRepo.delete(session)
            em.flush()
        }
    }

    @Transactional
    fun freeSessionSubjec(id: Long) {
        val sessionSubject: SessionSubject? = em.find(SessionSubject::class.java, id)

        if (sessionSubject != null) {
            val session:SessionSubject = sessionSubject
            session.auditorium?.sessionSubjects?.remove(session)
            session.auditorium = null
            session.date = -1
            sessionSubjectRepo.save(session)
            em.flush()
        }
    }
}