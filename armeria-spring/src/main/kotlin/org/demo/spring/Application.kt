package org.demo.spring

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.boot.SpringApplication

@SpringBootApplication
class Application {
    private val log = LoggerFactory.getLogger(Application::class.java)
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
//        SpringApplication.run(Application::class.java, *args)
}

//object Main {
//    @JvmStatic
//    fun main(args: Array<String>) {
//        runApplication<Application>(*args)
////        SpringApplication.run(Application::class.java, *args)
//    }
//}