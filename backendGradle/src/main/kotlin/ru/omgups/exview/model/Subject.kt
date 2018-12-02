package ru.omgups.exview.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Subject (var name: String = ""){
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long = 0

   @ManyToMany//(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
   var lectors: MutableSet<Lector> = HashSet()

    @ManyToMany//(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    var studentsGroup: MutableSet<StudentsGroup> = HashSet()

//    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
//    var cathedra: Cathedra = Cathedra()

    @OneToMany//( fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    var sessionSubjects: MutableSet<SessionSubject> = HashSet()
}