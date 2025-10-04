package org.example


fun printArtistInfo(artist: Artist) {
    println("\n" + "=".repeat(50))
    println("INFORMACION DEL ARTISTA")
    println("=".repeat(50))
    println("ID: ${artist.id}")
    println("Nombre: ${artist.name}")
    println("Generos: ${artist.genres.joinToString(", ").ifEmpty { "No especificados" }}")
    println("Popularidad: ${artist.popularity}/100")
    println("Seguidores: ${artist.followers?.total ?: "N/A"}")
}

fun printTrackInfo(track: Track) {
    println("\n" + "=".repeat(50))
    println("INFORMACION DE LA CANCION")
    println("=".repeat(50))
    println("ID: ${track.id}")
    println("Nombre: ${track.name}")
    println("Artistas: ${track.artists.joinToString(", ") { it.name }}")
    println("Album: ${track.album.name}")
    println("Duracion: ${formatDuration(track.durationMs)}")
    println("Popularidad: ${track.popularity}/100")
    println("Explicito: ${if (track.explicit) "Si" else "No"}")
    println("Numero de track: ${track.trackNumber}")
    println("Preview URL: ${track.previewUrl ?: "No disponible"}")
}

fun printAlbumInfo(album: Album) {
    println("\n" + "=".repeat(50))
    println("INFORMACION DEL ALBUM")
    println("=".repeat(50))
    println("ID: ${album.id}")
    println("Nombre: ${album.name}")
    println("Artistas: ${album.artists.joinToString(", ") { it.name }}")
    println("Tipo: ${album.albumType}")
    println("Fecha de lanzamiento: ${album.releaseDate}")
    println("Total de tracks: ${album.totalTracks}")
    println("Popularidad: ${album.popularity}/100")
    println("Sello discografico: ${album.label ?: "No especificado"}")
}

fun printPlaylistInfo(playlist: Playlist) {
    println("\n" + "=".repeat(50))
    println("INFORMACION DE LA PLAYLIST")
    println("=".repeat(50))
    println("ID: ${playlist.id}")
    println("Nombre: ${playlist.name}")
    println("Descripcion: ${playlist.description ?: "Sin descripcion"}")
    println("Creada por: ${playlist.owner.displayName ?: playlist.owner.id}")
    println("Publica: ${if (playlist.public == true) "Si" else if (playlist.public == false) "No" else "No especificado"}")
    println("Colaborativa: ${if (playlist.collaborative) "Si" else "No"}")
    println("Seguidores: ${playlist.followers.total}")
    println("Total de canciones: ${playlist.tracks.total}")
}
