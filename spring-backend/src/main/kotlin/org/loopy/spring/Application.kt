package org.loopy.spring

import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = arrayOf("org.loopy.spring.*"))
//@EnableConfigurationProperties(Properties::class)
class Application {
    private val log = LoggerFactory.getLogger(Application::class.java)
}

//object Main {
//    @JvmStatic
//    fun main(args: Array<String>) {
//        runApplication<Application>(*args)
////        SpringApplication.run(Application::class.java, *args)
//    }
//}

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
//        setBannerMode(Banner.Mode.OFF)
    }
}

//object Main {
//    @JvmStatic
//    fun main(args: Array<String>) {
//        runApplication<Application>(*args)
////        SpringApplication.run(Application::class.java, *args)
//    }
//}