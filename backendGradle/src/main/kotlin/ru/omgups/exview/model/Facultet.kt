package ru.omgups.exview.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*


@Entity
class Facultet(var name: String = "") {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long = 0


    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var cathedrs: MutableSet<Cathedra> = HashSet()

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var studentsGroup: MutableSet<StudentsGroup> = HashSet()
}