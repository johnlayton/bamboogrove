package org.demo.armeria

import com.linecorp.armeria.client.Clients;
import com.linecorp.armeria.client.ClientFactoryBuilder
import com.linecorp.armeria.client.HttpClient
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import java.nio.charset.Charset


object Application {
  @Throws(Exception::class)
  @JvmStatic
  fun main(args: Array<String>) {

    val clientFactory = ClientFactoryBuilder()
            .sslContextCustomizer {
              b -> b.trustManager(InsecureTrustManagerFactory.INSTANCE)
            }
            .build()

    val helloService = Clients.newClient(clientFactory, "gproto+https://127.0.0.1:8443/", HelloServiceGrpc.HelloServiceBlockingStub::class.java)
    helloService.callOptions
    val request = Greeting.HelloRequest.newBuilder().setName("Armerian World").build();
    val reply = helloService.hello(request)

    println(reply.message)

    println(HttpClient.of(clientFactory, "https://127.0.0.1:8443/").get("/users")
            .aggregate().join().content().toString(Charset.defaultCharset()));
  }
}
