package ru.omgups.exview.model

import javax.persistence.*


@Entity
class SessionSubject {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long = 0

    var day: Int = -1 // заменить впоследствии на java.util.Date либо иное

    @ManyToOne()
    var auditorium: Auditorium? = null

    @ManyToOne()
    var lector: Lector? = null

    @ManyToOne()
    var studentsGroup: StudentsGroup? = null

    @ManyToOne()
    var subject: Subject? = null
}