package org.demo.armeria

import com.linecorp.armeria.common.HttpHeaderNames
import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.common.SessionProtocol.HTTPS
import com.linecorp.armeria.common.SessionProtocol.PROXY
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.common.grpc.protocol.GrpcHeaderNames
import com.linecorp.armeria.server.ServerBuilder
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Produces
import com.linecorp.armeria.server.cors.CorsServiceBuilder.forOrigin
import com.linecorp.armeria.server.docs.DocServiceBuilder
import com.linecorp.armeria.server.grpc.GrpcServiceBuilder
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration

object Application {
    private val logger = LoggerFactory.getLogger(Application::class.java)

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val corsBuilder =
                forOrigin("http://localhost:3000")
                        .allowRequestMethods(HttpMethod.POST) // Allow POST method.
                        // Allow Content-type and X-GRPC-WEB headers.
                        .allowRequestHeaders(HttpHeaderNames.CONTENT_TYPE,
                                HttpHeaderNames.of("X-GRPC-WEB"))
                        // Expose trailers of the HTTP response to the client.
                        .exposeHeaders(GrpcHeaderNames.GRPC_STATUS,
                                GrpcHeaderNames.GRPC_MESSAGE,
                                GrpcHeaderNames.ARMERIA_GRPC_THROWABLEPROTO_BIN)

        val sb = ServerBuilder()
        val server = sb
//                .http(8080)
                .https(8443)
                .tlsSelfSigned()
                //.tls(new File("certificate.crt"), new File("private.key"), "myPassphrase")
                .service(GrpcServiceBuilder()
                        .addService(object : HelloServiceGrpc.HelloServiceImplBase() {
                            override fun hello(request: Greeting.HelloRequest,
                                               response: StreamObserver<Greeting.HelloReply>) {
                                val reply = Greeting.HelloReply.newBuilder()
                                        .setMessage("Hello, " + request.getName() + '!'.toString())
                                        .build()
                                response.onNext(reply)
                                response.onCompleted()
                            }
                        })
                        .addService(object : UserServiceGrpc.UserServiceImplBase() {
                            override fun list(request: Users.Empty,
                                              response: StreamObserver<Users.User>) {
                                Flux.interval(Duration.ofSeconds(1))
                                        .take(5)
                                        .map { index ->
                                            Users.User.newBuilder()
                                                    .setId(index)
                                                    .setFirstName("firstName")
                                                    .setLastName("lastName")
                                                    .build()
                                        }
                                        // You can make your Flux/Mono publish the signals in the RequestContext-aware executor.
                                        .publishOn(Schedulers.fromExecutor(ServiceRequestContext.current().contextAwareExecutor()))
                                        .subscribe({ user ->
                                            // Confirm this callback is being executed on the RequestContext-aware executor.
                                            ServiceRequestContext.current()
                                            response.onNext(user)
                                        }, { cause ->
                                            // Confirm this callback is being executed on the RequestContext-aware executor.
                                            ServiceRequestContext.current()
                                            response.onError(cause)
                                        }, {
                                            // Confirm this callback is being executed on the RequestContext-aware executor.
                                            ServiceRequestContext.current()
                                            response.onCompleted()
                                        })
                            }
                        })
                        .addService(ProtoReflectionService.newInstance())
                        .supportedSerializationFormats(GrpcSerializationFormats.values())
                        .enableUnframedRequests(true)
                        .build(),
                        corsBuilder.newDecorator()
                )
//                .annotatedService(object : Any() {
//                    @Get("/greet")
//                    @Produces("application/json;charset=UTF-8")
//                    fun greetGet(@Param("name") name: String): HttpResponse {
//                        return HttpResponse.of(HttpStatus.OK, MediaType.JSON_UTF_8, "{\"name\":\"%s\"}", name)
//                    }
//
//                    @Post("/greet7")
//                    @Consumes("application/x-www-form-urlencoded")
//                    fun greetPost(@Param("name") name: String): HttpResponse {
//                        return HttpResponse.of(HttpStatus.OK)
//                    }
//                })
                .annotatedService(object : Any() {
                    @Get("/users")
                    @Produces("application/json;charset=UTF-8")
                    fun users(): List<User> {
                        return listOf(
                            User(1L, "firstName", "lastName"),
                            User(2L, "firstName", "lastName")
                        );
                    }
                })
                .serviceUnder("/docs", DocServiceBuilder()
                        .exampleRequestForMethod(HelloServiceGrpc.SERVICE_NAME, "Hello", // Method name
                                Greeting.HelloRequest.newBuilder().setName("Armeria").build())
                        .exampleRequestForMethod(UserServiceGrpc.SERVICE_NAME, "List", // Method name
                                Greeting.HelloRequest.newBuilder().setName("Armeria").build())
                    .build()
                )
//                .port(8080, PROXY, HTTP)
                .port(8443, PROXY, HTTPS)
                .build()

        Runtime.getRuntime().addShutdownHook(Thread {
            logger.info("Server has been stopped.")
            server.stop().join()
        })

        server.start().join()

        val localAddress = server.activePort().get().localAddress()
        val isLocalAddress = localAddress.address.isAnyLocalAddress || localAddress.address.isLoopbackAddress

        logger.info("Server has been started. Serving at http://{}:{}",
                if (isLocalAddress) "127.0.0.1" else localAddress.hostString, localAddress.port)

    }

}
