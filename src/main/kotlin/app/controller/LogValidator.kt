package app.controller

import app.model.Log
import app.model.LogLevel
import app.model.LogEntry
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator
import java.time.format.DateTimeParseException

@Component
class LogValidator: Validator {

    private val dateField = "dt"
    private val levelField = "level"

    override fun supports(clazz: Class<*>): Boolean {
        return LogEntry::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val log: LogEntry = target as LogEntry
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, dateField, dateField, "Date field cannot be empty")
        if (!errors.hasFieldErrors(dateField)) {
            try {
                Log.dateFormat.parse(log.dt)
            } catch (ex: DateTimeParseException) {
                errors.rejectValue(dateField, dateField, "Date in wrong format: ${log.dt}")
            }
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, levelField, levelField, "Level field cannot be empty")
        if (!errors.hasFieldErrors(levelField) && LogLevel.values().find { it.name == log.level } == null) {
            val enumString = LogLevel.values().joinToString { it.name }
            errors.rejectValue(levelField, levelField,"Log level '${log.level}' is wrong: should be one of $enumString")
        }
    }
}