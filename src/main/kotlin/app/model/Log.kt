package app.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
class Log(): IdEntity() {

    companion object {
        val dateFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    }

    lateinit var author: String

    lateinit var dt: LocalDate

    lateinit var level: LogLevel

    lateinit var message: String

    constructor(req: LogEntry, author: String): this() {
        dt = LocalDate.parse(req.dt, dateFormat)
        level = LogLevel.valueOf(req.level)
        message = req.message
        this.author = author
    }

    fun toEntry(): LogEntry {
        val res = LogEntry()
        res.level = level.toString()
        res.message = message
        res.dt = dt.toString()
        return res
    }
}

@MappedSuperclass
open class IdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

}

class LogEntry {
    var dt: String = ""

    var level: String = ""

    var message: String = ""
}