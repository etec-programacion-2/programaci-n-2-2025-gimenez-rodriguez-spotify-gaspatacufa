package org.example

class Consola(private val spotify: SpotifyApiClient) { // clase que maneja la consola interactiva

    suspend fun run() { // funcion que inicia la consola
        Ejecutar( // crea un objeto ejecutar con las rutas de los archivos
            spotify, // pasa el cliente de spotify
            "database/artistas.txt", 
            "database/albumes.txt", 
            "database/pistas.txt", 
            "database/playlists.txt" 
        ).ejecutar() 
    }
}