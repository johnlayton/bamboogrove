package org.demo.armeria

import com.linecorp.armeria.client.Clients;

object Application {
  @Throws(Exception::class)
  @JvmStatic
  fun main(args: Array<String>) {
    val helloService = Clients.newClient("gproto+http://127.0.0.1:8080/", HelloServiceGrpc.HelloServiceBlockingStub::class.java)
    helloService.callOptions
    val request = Greeting.HelloRequest.newBuilder().setName("Armerian World").build();
    val reply = helloService.hello(request)

    println(reply.message)
  }
}
