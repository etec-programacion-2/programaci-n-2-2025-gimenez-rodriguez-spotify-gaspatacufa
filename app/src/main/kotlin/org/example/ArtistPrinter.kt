package org.example

class ArtistPrinter(private val artist: Artist) { // clase que imprime info de un artista
    fun print() { // funcion que muestra los datos del artista
        println("\n" + "=".repeat(50)) // imprime linea de separacion
        println("INFORMACION DEL ARTISTA") // imprime titulo
        println("=".repeat(50)) // imprime linea de separacion
        println("ID: ${artist.id}") // imprime el id
        println("Nombre: ${artist.name}") // imprime el nombre
        println("Generos: ${artist.genres.joinToString(", ").ifEmpty { "No especificados" }}") // imprime generos separados por coma, o mensaje si esta vacio
        println("Popularidad: ${artist.popularity}/100") // imprime popularidad sobre 100
        println("Seguidores: ${artist.followers?.total ?: "N/A"}") // imprime seguidores, o N/A si es null
    }
}
