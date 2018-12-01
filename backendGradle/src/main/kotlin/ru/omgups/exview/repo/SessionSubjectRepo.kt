package ru.omgups.exview.repo

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.omgups.exview.model.SessionSubject

@Repository
interface SessionSubjectRepo : PagingAndSortingRepository <SessionSubject, Long> {


}