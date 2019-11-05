import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
//    id("org.jetbrains.kotlin.jvm") version "1.3.41"

    kotlin("jvm")

    id("com.google.protobuf") version "0.8.10"
    id("com.google.cloud.tools.jib") version "1.3.0"
    idea
}

repositories {
    jcenter()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.linecorp.armeria:armeria:0.90.2")
    implementation("com.linecorp.armeria:armeria-grpc:0.90.2")

    implementation("com.google.protobuf:protobuf-java:3.6.1")

    implementation( "io.projectreactor:reactor-core:3.2.11.RELEASE")

    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.slf4j:log4j-over-slf4j:1.7.28")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

java {
    sourceCompatibility = JavaVersion.VERSION_12
    targetCompatibility = JavaVersion.VERSION_12
}

application {
    mainClassName = "org.demo.armeria.Application"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "12"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.8.0"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.15.1"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

jib {
    from {
        image = "openjdk:12-alpine"
    }
    container {
        ports = listOf("8080", "8443")
        mainClass = "org.demo.armeria.Application"
        jvmFlags = listOf(
                "-server",
                "-Djava.awt.headless=true",
                "-XX:InitialRAMFraction=2",
                "-XX:MinRAMFraction=2",
                "-XX:MaxRAMFraction=2",
                "-XX:+UseG1GC",
                "-XX:MaxGCPauseMillis=100",
                "-XX:+UseStringDeduplication"
        )
    }
}

