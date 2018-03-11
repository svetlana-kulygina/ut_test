package app.model

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface LogRepository : PagingAndSortingRepository<Log, Long> {

}