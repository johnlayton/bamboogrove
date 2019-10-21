package org.demo.spring.controller

import org.demo.spring.config.SimpleConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Flux

@Controller
class SimpleController
@Autowired constructor(private val configuration: SimpleConfiguration) {

    @GetMapping(path = ["/numbers"], produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    @ResponseBody
    fun getNumbers(): Flux<String> {
        return Flux.range(1, 100)
                .map { configuration.title + it + "\\n"}
    }

}
