plugins {
    alias(libs.plugins.kotlin.jvm) // activa kotlin para la jvm
    alias(libs.plugins.kotlin.serialization) // activa la serializacion de kotlin
    application // activa el plugin de aplicacion para ejecutar el programa
}

repositories {
    mavenCentral() // busca las dependencias en maven central
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5") // libreria para hacer tests con junit5
    testImplementation(libs.junit.jupiter.engine) // motor de junit5 para ejecutar los tests
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") // lanzador de la plataforma de tests

    implementation(libs.ktor.client.core) // nucleo del cliente http de ktor
    implementation(libs.ktor.client.cio) // motor cio para ktor que hace las peticiones http
    implementation(libs.ktor.client.content.negotiation) // negociacion de contenido para enviar y recibir json
    implementation(libs.ktor.serialization.kotlinx.json) // serializador json de kotlinx para ktor
    implementation(libs.slf4j.simple) // logger simple para ver mensajes de log
    
    implementation("io.ktor:ktor-server-core:2.3.7") // nucleo del servidor web de ktor
    implementation("io.ktor:ktor-server-netty:2.3.7") // motor netty para el servidor web

    implementation("org.xerial:sqlite-jdbc:3.44.1.0") // driver jdbc para conectarse a sqlite
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21) // usa java version 21
    }
}

application {
    mainClass = "org.example.AppKt" // clase principal que se ejecuta al iniciar el programa
}

tasks.named<Test>("test") {
    useJUnitPlatform() // usa junit platform para los tests
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in` // permite leer del teclado cuando se ejecuta el programa
}