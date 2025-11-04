package org.example

class Consola(private val spotify: SpotifyApiClient) { // clase que maneja la consola interactiva

    suspend fun run() { // funcion que inicia la consola
        Ejecutar( // crea un objeto ejecutar con las rutas de los archivos
            spotify, // pasa el cliente de spotify
            "database/artistas.txt", // ruta al archivo de ids de artistas
            "database/albumes.txt", // ruta al archivo de ids de albumes
            "database/pistas.txt", // ruta al archivo de ids de pistas
            "database/playlists.txt" // ruta al archivo de ids de playlists
        ).ejecutar() // ejecuta el menu interactivo
    }
}