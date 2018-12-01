package ru.omgups.exview.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Subject (var name: String = ""){
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long = 0

   @ManyToMany(fetch = FetchType.LAZY)//, cascade = [CascadeType.ALL])
   var lectors: MutableSet<Lector> = HashSet()

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var studentsGroup: MutableSet<StudentsGroup> = HashSet()

//    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
//    var cathedra: Cathedra = Cathedra()

}