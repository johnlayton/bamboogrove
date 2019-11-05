package org.loopy.spring

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
class Properties {
    var appName: String? = null
}