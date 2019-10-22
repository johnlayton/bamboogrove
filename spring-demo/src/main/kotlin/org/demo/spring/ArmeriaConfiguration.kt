package org.demo.spring

import com.linecorp.armeria.client.ClientFactory
import com.linecorp.armeria.client.ClientFactoryBuilder
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerHttpClient
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerStrategy
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.common.HttpResponse
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean

import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import com.linecorp.armeria.spring.web.reactive.ArmeriaClientConfigurator
import io.netty.handler.ssl.util.InsecureTrustManagerFactory


@Configuration
class ArmeriaConfiguration {

    @Bean
    fun armeriaServerConfigurator(): ArmeriaServerConfigurator {
        // Customize the server using the given ServerBuilder. For example:
        return ArmeriaServerConfigurator { builder ->
            builder.serviceUnder("/docs", DocService())

            // Log every message which the server receives and responds.
            builder.decorator(LoggingService.newDecorator<HttpRequest, HttpResponse>())

            // Write access log after completing a request.
            builder.accessLogWriter(AccessLogWriter.combined(), false)
        }
    }

    @Bean
    fun clientFactory(): ClientFactory {
        return ClientFactoryBuilder().sslContextCustomizer(
                { b -> b.trustManager(InsecureTrustManagerFactory.INSTANCE) }).build()
    }

    @Bean
    fun armeriaClientConfigurator(clientFactory: ClientFactory): ArmeriaClientConfigurator {
        // Customize the client using the given HttpClientBuilder. For example:
        return ArmeriaClientConfigurator { builder ->
            // Use a circuit breaker for each remote host.
            val strategy = CircuitBreakerStrategy.onServerErrorStatus()
            builder.decorator(CircuitBreakerHttpClient.builder(strategy)
                    .newDecorator())

            // Set a custom client factory.
            builder.factory(clientFactory)
        }
    }
}