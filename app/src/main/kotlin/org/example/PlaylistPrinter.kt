package org.example

class PlaylistPrinter(private val playlist: Playlist) { // clase que imprime info de una playlist
    fun print() { // funcion que muestra los datos de la playlist
        println("\n" + "=".repeat(50)) // imprime linea de separacion
        println("INFORMACION DE LA PLAYLIST") // imprime titulo
        println("=".repeat(50)) // imprime linea de separacion
        println("ID: ${playlist.id}") // imprime el id
        println("Nombre: ${playlist.name}") // imprime el nombre
        println("Descripcion: ${playlist.description ?: "Sin descripcion"}") // imprime descripcion, o mensaje si es null
        println("Creada por: ${playlist.owner.displayName ?: playlist.owner.id}") // imprime nombre del creador, o su id si no tiene nombre
        println("Publica: ${if (playlist.public == true) "Si" else if (playlist.public == false) "No" else "No especificado"}") // imprime si es publica
        println("Colaborativa: ${if (playlist.collaborative) "Si" else "No"}") // imprime si es colaborativa
        println("Seguidores: ${playlist.followers.total}") // imprime cantidad de seguidores
        println("Total de canciones: ${playlist.tracks.total}") // imprime cantidad de canciones
    }
}
