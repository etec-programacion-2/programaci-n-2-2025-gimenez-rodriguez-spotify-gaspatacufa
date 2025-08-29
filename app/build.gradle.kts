plugins {
    kotlin("jvm") version "1.9.25"
    application
}

repositories {
    mavenCentral()
}

val ktor_version = "2.3.4"

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("org.example.ApplicationKt")
}

// 🔧 Forzar toolchain Java 21
kotlin {
    jvmToolchain(21)
}
