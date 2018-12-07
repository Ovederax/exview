package ru.omgups.exview.model

import javax.persistence.*


@Entity
class SessionSubject {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long = 0

    @ManyToOne
    var auditorium: Auditorium? = null

    var date: Int = -1

    @ManyToOne()
    var lector: Lector? = null

    @ManyToOne()
    var studentsGroup: StudentsGroup? = null

    @ManyToOne()
    var subject: Subject? = null
}