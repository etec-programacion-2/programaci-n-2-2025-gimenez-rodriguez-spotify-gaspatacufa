package org.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun startWebServer(spotifyClient: SpotifyApiClient) {
    embeddedServer(Netty, port = 8080) {
        routing {
            
            // Página principal
            get("/") {
                val html = leerArchivo("static/index.html")
                call.respondText(html, ContentType.Text.Html)
            }

            // API: Buscar Artista por nombre
            get("/api/artista") {
                val nombre = call.request.queryParameters["nombre"] ?: ""
                
                if (nombre.isBlank()) {
                    call.respondText("Error: Nombre requerido", status = HttpStatusCode.BadRequest)
                    return@get
                }
                
                // Buscar en la base de datos
                val spotifyId = Database.buscarArtistaPorNombre(nombre)
                
                if (spotifyId == null) {
                    call.respondText("Artista '$nombre' no encontrado en la base de datos", status = HttpStatusCode.NotFound)
                    return@get
                }
                
                // Consultar en Spotify API
                val artist = spotifyClient.getArtist(spotifyId)
                
                if (artist == null) {
                    call.respondText("Error al consultar Spotify", status = HttpStatusCode.InternalServerError)
                    return@get
                }
                
                // Guardar/actualizar en BD
                Database.guardarArtista(artist)
                
                val html = leerArchivo("static/artista.html")
                    .replace("{{nombre}}", artist.name)
                    .replace("{{id}}", artist.id)
                    .replace("{{generos}}", artist.genres.joinToString(", ").ifEmpty { "No especificados" })
                    .replace("{{popularidad}}", artist.popularity.toString())
                    .replace("{{seguidores}}", artist.followers?.total?.toString() ?: "N/A")
                    .replace("{{url}}", artist.externalUrls?.spotify ?: "#")
                
                call.respondText(html, ContentType.Text.Html)
            }

            // API: Buscar Track por nombre
            get("/api/track") {
                val nombre = call.request.queryParameters["nombre"] ?: ""
                
                if (nombre.isBlank()) {
                    call.respondText("Error: Nombre requerido", status = HttpStatusCode.BadRequest)
                    return@get
                }
                
                val spotifyId = Database.buscarCancionPorNombre(nombre)
                
                if (spotifyId == null) {
                    call.respondText("Canción '$nombre' no encontrada en la base de datos", status = HttpStatusCode.NotFound)
                    return@get
                }
                
                val track = spotifyClient.getTrack(spotifyId)
                
                if (track == null) {
                    call.respondText("Error al consultar Spotify", status = HttpStatusCode.InternalServerError)
                    return@get
                }
                
                val html = leerArchivo("static/track.html")
                    .replace("{{nombre}}", track.name)
                    .replace("{{id}}", track.id)
                    .replace("{{artistas}}", track.artists.joinToString(", ") { it.name })
                    .replace("{{album}}", track.album.name)
                    .replace("{{duracion}}", formatDuration(track.durationMs))
                    .replace("{{popularidad}}", track.popularity.toString())
                    .replace("{{explicito}}", if (track.explicit) "Sí" else "No")
                    .replace("{{preview}}", track.previewUrl ?: "")
                
                call.respondText(html, ContentType.Text.Html)
            }

            // API: Buscar Álbum por nombre
            get("/api/album") {
                val nombre = call.request.queryParameters["nombre"] ?: ""
                
                if (nombre.isBlank()) {
                    call.respondText("Error: Nombre requerido", status = HttpStatusCode.BadRequest)
                    return@get
                }
                
                val spotifyId = Database.buscarAlbumPorNombre(nombre)
                
                if (spotifyId == null) {
                    call.respondText("Álbum '$nombre' no encontrado en la base de datos", status = HttpStatusCode.NotFound)
                    return@get
                }
                
                val album = spotifyClient.getAlbum(spotifyId)
                
                if (album == null) {
                    call.respondText("Error al consultar Spotify", status = HttpStatusCode.InternalServerError)
                    return@get
                }
                
                val html = leerArchivo("static/album.html")
                    .replace("{{nombre}}", album.name)
                    .replace("{{id}}", album.id)
                    .replace("{{artistas}}", album.artists.joinToString(", ") { it.name })
                    .replace("{{tipo}}", album.albumType)
                    .replace("{{fecha}}", album.releaseDate)
                    .replace("{{tracks}}", album.totalTracks.toString())
                    .replace("{{popularidad}}", album.popularity.toString())
                    .replace("{{sello}}", album.label ?: "No especificado")
                
                call.respondText(html, ContentType.Text.Html)
            }

            // API: Buscar Playlist por nombre
            get("/api/playlist") {
                val nombre = call.request.queryParameters["nombre"] ?: ""
                
                if (nombre.isBlank()) {
                    call.respondText("Error: Nombre requerido", status = HttpStatusCode.BadRequest)
                    return@get
                }
                
                val spotifyId = Database.buscarPlaylistPorNombre(nombre)
                
                if (spotifyId == null) {
                    call.respondText("Playlist '$nombre' no encontrada en la base de datos", status = HttpStatusCode.NotFound)
                    return@get
                }
                
                val playlist = spotifyClient.getPlaylist(spotifyId)
                
                if (playlist == null) {
                    call.respondText("Error al consultar Spotify", status = HttpStatusCode.InternalServerError)
                    return@get
                }
                
                val html = leerArchivo("static/playlist.html")
                    .replace("{{nombre}}", playlist.name)
                    .replace("{{id}}", playlist.id)
                    .replace("{{descripcion}}", playlist.description ?: "Sin descripción")
                    .replace("{{creador}}", playlist.owner.displayName ?: playlist.owner.id)
                    .replace("{{publica}}", when (playlist.public) {
                        true -> "Sí"
                        false -> "No"
                        null -> "No especificado"
                    })
                    .replace("{{colaborativa}}", if (playlist.collaborative) "Sí" else "No")
                    .replace("{{seguidores}}", playlist.followers.total.toString())
                    .replace("{{canciones}}", playlist.tracks.total.toString())
                
                call.respondText(html, ContentType.Text.Html)
            }
        }
        
        println("\n" + "=".repeat(70))
        println("🌐 Servidor web iniciado en: http://localhost:8080")
        println("=".repeat(70))
    }.start(wait = true)
}

fun leerArchivo(ruta: String): String {
    return object {}.javaClass.classLoader
        .getResourceAsStream(ruta)
        ?.bufferedReader()
        ?.readText()
        ?: "Archivo no encontrado: $ruta"
}