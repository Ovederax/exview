package ru.omgups.exview.model

import javax.persistence.*

@Entity
data class StudentsGroup(var name: String = "") {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long = 0

    @OneToMany//(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    var sessionSubjects: MutableSet<SessionSubject> = HashSet()

}