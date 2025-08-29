package org.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    routing {
        get("/hello") {
            call.respondText("Hola, el server Ktor anda perfecto 🚀")
        }
    }
}
