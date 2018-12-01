package ru.omgups.exview.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.omgups.exview.model.Auditorium

@Repository
interface AuditoriumRepo: PagingAndSortingRepository<Auditorium, Long> {

}