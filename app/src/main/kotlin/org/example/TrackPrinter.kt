package org.example

import org.example.FormatDuration

class TrackPrinter(private val track: Track) { // clase que imprime info de una cancion
    fun print() { // funcion que muestra los datos de la cancion
        println("\n" + "=".repeat(50)) // imprime linea de separacion
        println("INFORMACION DE LA CANCION") // imprime titulo
        println("=".repeat(50)) // imprime linea de separacion
        println("ID: ${track.id}") // imprime el id
        println("Nombre: ${track.name}") // imprime el nombre
        println("Artistas: ${track.artists.joinToString(", ") { it.name }}") // imprime artistas separados por coma
        println("Album: ${track.album.name}") // imprime el nombre del album
        println("Duracion: ${FormatDuration(track.durationMs).run()}") // imprime duracion formateada
        println("Popularidad: ${track.popularity}/100") // imprime popularidad sobre 100
        println("Explicito: ${if (track.explicit) "Si" else "No"}") // imprime si o no segun sea explicito
        println("Numero de track: ${track.trackNumber}") // imprime numero de track
        println("Preview URL: ${track.previewUrl ?: "No disponible"}") // imprime url de preview, o mensaje si es null
    }
}
