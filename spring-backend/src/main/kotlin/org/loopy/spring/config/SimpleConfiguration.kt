package org.loopy.spring.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SimpleConfiguration {

    init {
        println (".... init simple configuration ....")
    }


    @Value("\${greeting.title}")
    internal var title: String? = null

}