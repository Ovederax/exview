package ru.omgups.exview.repo

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.omgups.exview.model.Subject

@Repository
interface SubjectRepo : PagingAndSortingRepository<Subject,Long> {

}