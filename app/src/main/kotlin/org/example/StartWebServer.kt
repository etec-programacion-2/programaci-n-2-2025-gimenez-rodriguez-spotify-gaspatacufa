package org.example

class StartWebServer(private val spotifyClient: SpotifyApiClient) { // clase que inicia el servidor web
    fun run() { // funcion que arranca el servidor
        WebServer(spotifyClient).start() // crea instancia de webserver y llama a start
    }
}