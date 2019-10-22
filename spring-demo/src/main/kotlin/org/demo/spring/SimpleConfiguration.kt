package org.demo.spring

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SimpleConfiguration {

    @Value("\${greeting.title}")
    internal var title: String? = null

}