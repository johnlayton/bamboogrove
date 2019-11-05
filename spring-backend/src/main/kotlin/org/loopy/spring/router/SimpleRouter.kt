package org.loopy.spring.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

import org.springframework.web.reactive.function.BodyInserters.fromObject

@Configuration
class SimpleRouter {

    init {
        println (".... init simple router ....")
    }

    @Bean
    fun route() = router {
//        println (".... here ....")
        GET("/route") { _ -> ServerResponse.ok().body(fromObject(arrayOf(1, 2, 3))) }
    }
}