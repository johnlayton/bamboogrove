package org.demo.armeria

import com.linecorp.armeria.client.Clients;

fun main() {

  val helloService = Clients.newClient("gproto+https://127.0.0.1:8443/", HelloServiceGrpc.HelloServiceBlockingStub::class.java)
  helloService.callOptions
  val request = Greeting.HelloRequest.newBuilder().setName("Armerian World").build();
  val reply = helloService.hello(request)

  println(reply.message
  )
}