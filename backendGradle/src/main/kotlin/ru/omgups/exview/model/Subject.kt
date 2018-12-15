package ru.omgups.exview.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Subject (var name: String = ""){
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long = 0

   @ManyToMany
   var lectors: MutableSet<Lector> = HashSet()

    @ManyToMany
    var studentsGroup: MutableSet<StudentsGroup> = HashSet()

    @OneToMany
    var sessionSubjects: MutableSet<SessionSubject> = HashSet()
}