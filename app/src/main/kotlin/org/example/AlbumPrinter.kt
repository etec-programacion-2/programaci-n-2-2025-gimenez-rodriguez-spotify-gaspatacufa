package org.example

class AlbumPrinter(private val album: Album) { // clase que imprime info de un album
    fun print() { // funcion que muestra los datos del album
        println("\n" + "=".repeat(50)) // imprime linea de separacion
        println("INFORMACION DEL ALBUM") // imprime titulo
        println("=".repeat(50)) // imprime linea de separacion
        println("ID: ${album.id}") // imprime el id
        println("Nombre: ${album.name}") // imprime el nombre
        println("Artistas: ${album.artists.joinToString(", ") { it.name }}") // imprime artistas separados por coma
        println("Tipo: ${album.albumType}") // imprime el tipo de album
        println("Fecha de lanzamiento: ${album.releaseDate}") // imprime fecha de lanzamiento
        println("Total de tracks: ${album.totalTracks}") // imprime cantidad de canciones
        println("Popularidad: ${album.popularity}/100") // imprime popularidad sobre 100
        println("Sello discografico: ${album.label ?: "No especificado"}") // imprime sello, o mensaje si es null
    }
}
