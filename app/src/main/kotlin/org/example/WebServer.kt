package org.example

import org.example.FormatDuration // importa clase para formatear duracion
import io.ktor.server.application.* // importa clases del servidor
import io.ktor.server.engine.* // importa motor del servidor
import io.ktor.server.netty.* // importa implementacion netty
import io.ktor.server.response.* // importa funciones para responder
import io.ktor.server.routing.* // importa funciones para definir rutas
import io.ktor.http.* // importa tipos http

class WebServer(private val spotifyClient: SpotifyApiClient, private val port: Int = 9090) { // clase que maneja el servidor web, recibe cliente spotify y puerto (9090 por defecto)
    
    fun start() { // funcion que inicia el servidor
        embeddedServer(Netty, port = port) { // crea servidor web con netty en el puerto configurado
            routing { // define las rutas del servidor
                
                // pagina principal
                get("/") { // ruta raiz, pagina principal
                    val html = leerArchivo("static/index.html") // lee el archivo html de inicio
                    call.respondText(html, ContentType.Text.Html) // responde con el html
                }

                // api: buscar artista por nombre
                get("/api/artista") { // ruta para buscar artista
                    buscarArtista(call) // llama al metodo privado buscarartista
                }

                // api: buscar track por nombre
                get("/api/track") { // ruta para buscar cancion
                    buscarTrack(call) // llama al metodo privado buscartrack
                }

                // api: buscar album por nombre
                get("/api/album") { // ruta para buscar album
                    buscarAlbum(call) // llama al metodo privado buscaralbum
                }

                // api: buscar playlist por nombre
                get("/api/playlist") { // ruta para buscar playlist
                    buscarPlaylist(call) // llama al metodo privado buscarplaylist
                }
            }
            
            imprimirMensajeInicio() // imprime mensaje de que el servidor inicio
        }.start(wait = true) // inicia el servidor y espera
    }
    
    private suspend fun buscarArtista(call: ApplicationCall) { // metodo privado que busca un artista, recibe el call de ktor
        val nombre = call.request.queryParameters["nombre"] ?: "" // obtiene el parametro nombre de la url, o string vacio si no existe
        
        if (nombre.isBlank()) { // si el nombre esta vacio
            call.respondText("Error: Nombre requerido", status = HttpStatusCode.BadRequest) // responde con error 400
            return // sale de la funcion
        }
        
        val spotifyId = Database.buscarArtistaPorNombre(nombre) // busca el id del artista en la base de datos
        
        if (spotifyId == null) { // si no se encuentra el artista
            call.respondText("Artista '$nombre' no encontrado en la base de datos", status = HttpStatusCode.NotFound) // responde con error 404
            return // sale de la funcion
        }
        
        val artist = spotifyClient.getArtist(spotifyId) // consulta el artista en la api de spotify
        
        if (artist == null) { // si hay error al consultar
            call.respondText("Error al consultar Spotify", status = HttpStatusCode.InternalServerError) // responde con error 500
            return // sale de la funcion
        }
        
        val html = leerArchivo("static/artista.html") // lee la plantilla html de artista
            .replace("{{nombre}}", artist.name) // reemplaza el placeholder con el nombre del artista
            .replace("{{id}}", artist.id) // reemplaza el placeholder con el id
            .replace("{{generos}}", artist.genres.joinToString(", ").ifEmpty { "No especificados" }) // reemplaza con generos separados por coma
            .replace("{{popularidad}}", artist.popularity.toString()) // reemplaza con popularidad
            .replace("{{seguidores}}", artist.followers?.total?.toString() ?: "N/A") // reemplaza con seguidores o n/a si es null
            .replace("{{url}}", artist.externalUrls?.spotify ?: "#") // reemplaza con url de spotify o # si es null
        
        call.respondText(html, ContentType.Text.Html) // responde con el html completado
    }
    
    private suspend fun buscarTrack(call: ApplicationCall) { // metodo privado que busca una cancion, recibe el call de ktor
        val nombre = call.request.queryParameters["nombre"] ?: "" // obtiene el parametro nombre de la url
        
        if (nombre.isBlank()) { // si el nombre esta vacio
            call.respondText("Error: Nombre requerido", status = HttpStatusCode.BadRequest) // responde con error 400
            return // sale
        }
        
        val spotifyId = Database.buscarCancionPorNombre(nombre) // busca el id de la cancion en la base de datos
        
        if (spotifyId == null) { // si no se encuentra la cancion
            call.respondText("Canción '$nombre' no encontrada en la base de datos", status = HttpStatusCode.NotFound) // responde con error 404
            return // sale
        }
        
        val track = spotifyClient.getTrack(spotifyId) // consulta la cancion en la api de spotify
        
        if (track == null) { // si hay error al consultar
            call.respondText("Error al consultar Spotify", status = HttpStatusCode.InternalServerError) // responde con error 500
            return // sale
        }
        
        val html = leerArchivo("static/track.html") // lee la plantilla html de track
            .replace("{{nombre}}", track.name) // reemplaza con nombre de la cancion
            .replace("{{id}}", track.id) // reemplaza con id
            .replace("{{artistas}}", track.artists.joinToString(", ") { it.name }) // reemplaza con artistas separados por coma
            .replace("{{album}}", track.album.name) // reemplaza con nombre del album
            .replace("{{duracion}}", FormatDuration(track.durationMs).run()) // reemplaza con duracion formateada mm:ss
            .replace("{{popularidad}}", track.popularity.toString()) // reemplaza con popularidad
            .replace("{{explicito}}", if (track.explicit) "Sí" else "No") // reemplaza con si o no segun sea explicito
            .replace("{{preview}}", track.previewUrl ?: "") // reemplaza con url de preview o string vacio
        
        call.respondText(html, ContentType.Text.Html) // responde con el html completado
    }
    
    private suspend fun buscarAlbum(call: ApplicationCall) { // metodo privado que busca un album, recibe el call de ktor
        val nombre = call.request.queryParameters["nombre"] ?: "" // obtiene el parametro nombre de la url
        
        if (nombre.isBlank()) { // si el nombre esta vacio
            call.respondText("Error: Nombre requerido", status = HttpStatusCode.BadRequest) // responde con error 400
            return // sale
        }
        
        val spotifyId = Database.buscarAlbumPorNombre(nombre) // busca el id del album en la base de datos
        
        if (spotifyId == null) { // si no se encuentra el album
            call.respondText("Álbum '$nombre' no encontrado en la base de datos", status = HttpStatusCode.NotFound) // responde con error 404
            return // sale
        }
        
        val album = spotifyClient.getAlbum(spotifyId) // consulta el album en la api de spotify
        
        if (album == null) { // si hay error al consultar
            call.respondText("Error al consultar Spotify", status = HttpStatusCode.InternalServerError) // responde con error 500
            return // sale
        }
        
        val html = leerArchivo("static/album.html") // lee la plantilla html de album
            .replace("{{nombre}}", album.name) // reemplaza con nombre del album
            .replace("{{id}}", album.id) // reemplaza con id
            .replace("{{artistas}}", album.artists.joinToString(", ") { it.name }) // reemplaza con artistas separados por coma
            .replace("{{tipo}}", album.albumType) // reemplaza con tipo de album
            .replace("{{fecha}}", album.releaseDate) // reemplaza con fecha de lanzamiento
            .replace("{{tracks}}", album.totalTracks.toString()) // reemplaza con cantidad de canciones
            .replace("{{popularidad}}", album.popularity.toString()) // reemplaza con popularidad
            .replace("{{sello}}", album.label ?: "No especificado") // reemplaza con sello discografico o mensaje si es null
        
        call.respondText(html, ContentType.Text.Html) // responde con el html completado
    }
    
    private suspend fun buscarPlaylist(call: ApplicationCall) { // metodo privado que busca una playlist, recibe el call de ktor
        val nombre = call.request.queryParameters["nombre"] ?: "" // obtiene el parametro nombre de la url
        
        if (nombre.isBlank()) { // si el nombre esta vacio
            call.respondText("Error: Nombre requerido", status = HttpStatusCode.BadRequest) // responde con error 400
            return // sale
        }
        
        val spotifyId = Database.buscarPlaylistPorNombre(nombre) // busca el id de la playlist en la base de datos
        
        if (spotifyId == null) { // si no se encuentra la playlist
            call.respondText("Playlist '$nombre' no encontrada en la base de datos", status = HttpStatusCode.NotFound) // responde con error 404
            return // sale
        }
        
        val playlist = spotifyClient.getPlaylist(spotifyId) // consulta la playlist en la api de spotify
        
        if (playlist == null) { // si hay error al consultar
            call.respondText("Error al consultar Spotify", status = HttpStatusCode.InternalServerError) // responde con error 500
            return // sale
        }
        
        val html = leerArchivo("static/playlist.html") // lee la plantilla html de playlist
            .replace("{{nombre}}", playlist.name) // reemplaza con nombre de la playlist
            .replace("{{id}}", playlist.id) // reemplaza con id
            .replace("{{descripcion}}", playlist.description ?: "Sin descripción") // reemplaza con descripcion o mensaje si es null
            .replace("{{creador}}", playlist.owner.displayName ?: playlist.owner.id) // reemplaza con nombre del creador o su id si no tiene nombre
            .replace("{{publica}}", when (playlist.public) { // evalua el valor de public
                true -> "Sí" // si es true devuelve si
                false -> "No" // si es false devuelve no
                null -> "No especificado" // si es null devuelve no especificado
            })
            .replace("{{colaborativa}}", if (playlist.collaborative) "Sí" else "No") // reemplaza con si o no segun sea colaborativa
            .replace("{{seguidores}}", playlist.followers.total.toString()) // reemplaza con cantidad de seguidores
            .replace("{{canciones}}", playlist.tracks.total.toString()) // reemplaza con cantidad de canciones
        
        call.respondText(html, ContentType.Text.Html) // responde con el html completado
    }
    
    private fun leerArchivo(ruta: String): String { // metodo privado que lee un archivo del directorio resources
        return object {}.javaClass.classLoader // obtiene el classloader
            .getResourceAsStream(ruta) // abre el archivo como stream desde resources
            ?.bufferedReader() // lo convierte en buffered reader
            ?.readText() // lee todo el texto del archivo
            ?: "Archivo no encontrado: $ruta" // si no existe devuelve mensaje de error
    }
    
    private fun imprimirMensajeInicio() { // metodo privado que imprime mensaje de inicio del servidor
        println("\n" + "=".repeat(70)) // imprime linea de separacion de 70 caracteres
        println("🌐 Servidor web iniciado en: http://localhost:$port") // imprime mensaje con la url del servidor
        println("=".repeat(70)) // imprime linea de separacion de 70 caracteres
    }
}

// funcion de conveniencia para mantener compatibilidad
fun startWebServer(spotifyClient: SpotifyApiClient) { // funcion global que crea y arranca el servidor
    WebServer(spotifyClient).start() // crea instancia de webserver y llama a start
}