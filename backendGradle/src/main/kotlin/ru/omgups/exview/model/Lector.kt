package ru.omgups.exview.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Lector(var name: String = "") {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long = 0

    @ManyToMany//(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    var subjects: MutableSet<Subject> = HashSet()

    @ManyToOne//(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    var cathedra: Cathedra? = null

    @OneToMany//( fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    var sessionSubjects: MutableSet<SessionSubject> = HashSet()
}