package org.example

suspend fun main(args: Array<String>) {
    val spotify = Autenticacion().authenticate()

    if ("--web" in args) StartWebServer(spotify).run()
    else Consola(spotify).run()
}
