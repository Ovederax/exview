package ru.omgups.exview.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Lector(var name: String = "") {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long = 0

    @ManyToMany( fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var subjects: MutableSet<Subject> = HashSet()

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var cathedra: Cathedra? = null

    //лектор ведет занятие у группы и принимает у нее экзамен
    // причем ведет определенный предмет

    //был сделан отдельный класс связывающий эти сущеости
}