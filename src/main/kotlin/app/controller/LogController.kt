package app.controller

import app.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import javax.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder

@RestController
class LogController {

    class SimpleMessage(msg: String) {
        val message = msg
    }

    @Autowired
    private val logValidator: LogValidator? = null

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.validator = logValidator;
    }

    @Autowired
    private var logRepository: LogRepository? = null

    @RequestMapping("/")
    fun hello(): String {
        return "Hello"
    }

    @RequestMapping("/login", params = ["error"])
    @ResponseStatus(value= HttpStatus.UNAUTHORIZED)
    fun loginError(): SimpleMessage {
        return SimpleMessage("Access denied")
    }

    @RequestMapping("/accessError")
    @ResponseStatus(value= HttpStatus.FORBIDDEN)
    fun errror(): SimpleMessage {
        return SimpleMessage("User ${getUsername()} does not have access")
    }

    @GetMapping("/getLogs")
    fun getLogs(@PageableDefault(sort = ["dt"]) pageable: Pageable): Iterable<LogEntry> {
        return logRepository!!.findAll(pageable).content.map { it.toEntry() }
    }

    @PostMapping("/add")
    fun handleLog(@RequestBody @Valid logEntry: LogEntry, bindingResult: BindingResult): Any {
        if (!bindingResult.hasErrors()) {
            return saveLog(logEntry)
        } else {
            throw BindException(bindingResult);
        }
    }

    fun saveLog(logEntry: LogEntry): IdEntity {
        val id = IdEntity()
        id.id = logRepository!!.save(Log(logEntry, getUsername())).id
        return id
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    fun handle(bindingResult: BindingResult): Errors {
        val result = Errors()
        bindingResult.allErrors.forEach( { result.addError(it.code!!, it.defaultMessage) })
        return result
    }

    fun getUsername(): String {
        return SecurityContextHolder.getContext().authentication?.name ?: "unknown"
    }
}