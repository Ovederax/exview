package ru.omgups.exview.model

import javax.persistence.*


@Entity
class SessionSubject {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long = 0

    @ManyToOne//(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var lector: Lector = Lector()

    @ManyToOne( fetch = FetchType.LAZY)
    var studentGroup: StudentsGroup = StudentsGroup()

    @ManyToOne
    var subject: Subject = Subject()
}