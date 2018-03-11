package app

import org.springframework.boot.*
import org.springframework.boot.autoconfigure.*

@SpringBootApplication
class Application: CommandLineRunner {

    override fun run(vararg args: String?) {

    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}