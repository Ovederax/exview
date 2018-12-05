package ru.omgups.exview.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Cathedra(var name: String = "") {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    //@ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    //var facultet: Facultet = Facultet()

    @OneToMany//(orphanRemoval = false, fetch = FetchType.LAZY)//, cascade = [CascadeType.ALL])
    var lectors: MutableSet<Lector> = HashSet()

    @OneToMany//(orphanRemoval = false, fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var subjects: MutableSet<Subject> = HashSet()

    @OneToMany//(orphanRemoval = false, fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var auditoriums: MutableSet<Auditorium> = HashSet()

}