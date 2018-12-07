package ru.omgups.exview.model

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.annotation.Generated
import javax.persistence.*

@Entity
data class Auditorium (var name: String = ""){

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @OneToMany
    var sessionSubjects: MutableSet<SessionSubject> = HashSet()

    @ManyToOne
    var cathedra: Cathedra? = null
}