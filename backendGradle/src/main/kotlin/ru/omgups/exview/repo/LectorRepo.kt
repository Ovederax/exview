package ru.omgups.exview.repo

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.omgups.exview.model.Lector

@Repository
interface LectorRepo: PagingAndSortingRepository<Lector, Long> {

    fun findByName(name:String): Lector

}