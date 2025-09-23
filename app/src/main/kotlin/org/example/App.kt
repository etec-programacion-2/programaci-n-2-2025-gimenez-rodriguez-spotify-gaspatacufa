package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.*


@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int
)

@Serializable
data class Followers(
    val total: Int
)

@Serializable
data class TrackList(
    val href: String,
    val limit: Int,
    val next: String? = null,
    val offset: Int,
    val previous: String? = null,
    val total: Int,
    val items: List<SimpleTrack> = emptyList()
)

@Serializable
data class SimpleTrack(
    val id: String,
    val name: String,
    val artists: List<SimpleArtist>,
    @SerialName("duration_ms") val durationMs: Long,
    val explicit: Boolean = false,
    @SerialName("track_number") val trackNumber: Int,
    @SerialName("disc_number") val discNumber: Int = 1,
    @SerialName("preview_url") val previewUrl: String? = null,
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null
)

@Serializable
data class Copyright(
    val text: String,
    val type: String
)

@Serializable
data class PlaylistOwner(
    val id: String,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null,
    val followers: Followers? = null,
    val images: List<Image> = emptyList()
)

@Serializable
data class PlaylistTrackList(
    val href: String,
    val limit: Int,
    val next: String? = null,
    val offset: Int,
    val previous: String? = null,
    val total: Int,
    val items: List<PlaylistTrackItem> = emptyList()
)

@Serializable
data class PlaylistTrackItem(
    @SerialName("added_at") val addedAt: String? = null,
    @SerialName("added_by") val addedBy: PlaylistOwner? = null,
    @SerialName("is_local") val isLocal: Boolean = false,
    val track: PlaylistTrack? = null
)

@Serializable
data class PlaylistTrack(
    val id: String? = null,
    val name: String,
    val artists: List<SimpleArtist>,
    val album: SimpleAlbum? = null,
    @SerialName("duration_ms") val durationMs: Long,
    val explicit: Boolean = false,
    val popularity: Int = 0,
    @SerialName("preview_url") val previewUrl: String? = null,
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null,
    @SerialName("is_local") val isLocal: Boolean = false
)

@Serializable
data class SimpleArtist(
    val id: String,
    val name: String,
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null
)

@Serializable
data class SimpleAlbum(
    val id: String,
    val name: String,
    @SerialName("album_type") val albumType: String,
    val artists: List<SimpleArtist>,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("total_tracks") val totalTracks: Int = 0,
    val images: List<Image> = emptyList(),
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null
)

@Serializable
data class Image(
    val url: String,
    val height: Int? = null,
    val width: Int? = null
)

@Serializable
data class ExternalUrls(
    val spotify: String
)

class SpotifyApiClient(private val clientId: String, private val clientSecret: String) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    
    private var accessToken: String? = null
    private var tokenType: String? = null
    
    suspend fun authenticate(): Boolean {
        return try {
            println("🔑 Obteniendo access token...")
            val credentials = Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())
            
            val tokenResponse: TokenResponse = client.submitForm(
                url = "https://accounts.spotify.com/api/token",
                formParameters = parameters {
                    append("grant_type", "client_credentials")
                }
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Basic $credentials")
                    append(HttpHeaders.ContentType, "application/x-www-form-urlencoded")
                }
            }.body()
            
            accessToken = tokenResponse.accessToken
            tokenType = tokenResponse.tokenType
            
            println("✅ Token obtenido exitosamente")
            println("Expires in: ${tokenResponse.expiresIn} seconds")
            true
        } catch (e: Exception) {
            println("❌ Error al obtener token: ${e.message}")
            false
        }
    }
    
    suspend fun getArtist(artistId: String): Artist? {
        return try {
            println("\n🎤 Obteniendo información del artista...")
            val artist: Artist = client.get("https://api.spotify.com/v1/artists/$artistId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }.body()
            
            println("✅ Artista obtenido: ${artist.name}")
            artist
        } catch (e: Exception) {
            println("❌ Error al obtener artista: ${e.message}")
            null
        }
    }
    
    suspend fun getTrack(trackId: String): Track? {
        return try {
            println("\n🎵 Obteniendo información de la canción...")
            val track: Track = client.get("https://api.spotify.com/v1/tracks/$trackId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }.body()
            
            println("✅ Canción obtenida: ${track.name}")
            track
        } catch (e: Exception) {
            println("❌ Error al obtener canción: ${e.message}")
            null
        }
    }
    
    suspend fun getAlbum(albumId: String): Album? {
        return try {
            println("\n💿 Obteniendo información del álbum...")
            val album: Album = client.get("https://api.spotify.com/v1/albums/$albumId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }.body()
            
            println("✅ Álbum obtenido: ${album.name}")
            album
        } catch (e: Exception) {
            println("❌ Error al obtener álbum: ${e.message}")
            null
        }
    }
    
    suspend fun getPlaylist(playlistId: String): Playlist? {
        return try {
            println("\n📋 Obteniendo información de la playlist...")
            val playlist: Playlist = client.get("https://api.spotify.com/v1/playlists/$playlistId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }.body()
            
            println("✅ Playlist obtenida: ${playlist.name}")
            playlist
        } catch (e: Exception) {
            println("❌ Error al obtener playlist: ${e.message}")
            null
        }
    }
    
    fun close() {
        client.close()
    }
}


fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes}:${seconds.toString().padStart(2, '0')}"
}

fun printArtistInfo(artist: Artist) {
    println("\n" + "=".repeat(50))
    println("🎤 INFORMACIÓN DEL ARTISTA")
    println("=".repeat(50))
    println("ID: ${artist.id}")
    println("Nombre: ${artist.name}")
    println("Géneros: ${artist.genres.joinToString(", ").ifEmpty { "No especificados" }}")
    println("Popularidad: ${artist.popularity}/100")
    println("Seguidores: ${artist.followers?.total ?: "N/A"}")
}

fun printTrackInfo(track: Track) {
    println("\n" + "=".repeat(50))
    println("🎵 INFORMACIÓN DE LA CANCIÓN")
    println("=".repeat(50))
    println("ID: ${track.id}")
    println("Nombre: ${track.name}")
    println("Artistas: ${track.artists.joinToString(", ") { it.name }}")
    println("Álbum: ${track.album.name}")
    println("Duración: ${formatDuration(track.durationMs)}")
    println("Popularidad: ${track.popularity}/100")
    println("Explícito: ${if (track.explicit) "Sí" else "No"}")
    println("Número de track: ${track.trackNumber}")
    println("Preview URL: ${track.previewUrl ?: "No disponible"}")
}

fun printAlbumInfo(album: Album) {
    println("\n" + "=".repeat(50))
    println("💿 INFORMACIÓN DEL ÁLBUM")
    println("=".repeat(50))
    println("ID: ${album.id}")
    println("Nombre: ${album.name}")
    println("Artistas: ${album.artists.joinToString(", ") { it.name }}")
    println("Tipo: ${album.albumType}")
    println("Fecha de lanzamiento: ${album.releaseDate}")
    println("Total de tracks: ${album.totalTracks}")
    println("Popularidad: ${album.popularity}/100")
    println("Sello discográfico: ${album.label ?: "No especificado"}")
    

}

fun printPlaylistInfo(playlist: Playlist) {
    println("\n" + "=".repeat(50))
    println("📋 INFORMACIÓN DE LA PLAYLIST")
    println("=".repeat(50))
    println("ID: ${playlist.id}")
    println("Nombre: ${playlist.name}")
    println("Descripción: ${playlist.description ?: "Sin descripción"}")
    println("Creada por: ${playlist.owner.displayName ?: playlist.owner.id}")
    println("Pública: ${if (playlist.public == true) "Sí" else if (playlist.public == false) "No" else "No especificado"}")
    println("Colaborativa: ${if (playlist.collaborative) "Sí" else "No"}")
    println("Seguidores: ${playlist.followers.total}")
    println("Total de canciones: ${playlist.tracks.total}")
    

}


suspend fun main() {
    // ⚠️ REEMPLAZA CON TUS CREDENCIALES REALES
    val clientId = "43551abad28b4f9290ed67904ee20f5e"
    val clientSecret = "dd2408b1ccae4bdca9fd71735f6649eb"
    
    val spotifyClient = SpotifyApiClient(clientId, clientSecret)
    
    try {
        // Paso 1: Autenticarse
        if (!spotifyClient.authenticate()) {
            println("❌ No se pudo obtener el token de acceso")
            return
        }
        
        println("\n" + "=".repeat(70))
        println("🎼 INICIANDO CONSULTAS A LA API DE SPOTIFY")
        println("=".repeat(70))
        
        // IDs de ejemplo para las consultas
        val artistId = "0TnOYISbd1XYRBk9myaseg"     
        val trackId = "4u7EnebtmKWzUH433cf5Qv"      
        val albumId = "4aawyAB9vmqN3uQ7FjRGTy"      
        val playlistId = "3cEYpjA9oz9GiPac4AsH4n"   
        
        // Paso 2: Obtener información del artista
        spotifyClient.getArtist(artistId)?.let { artist ->
            printArtistInfo(artist)
        }
        
        // Paso 3: Obtener información de la canción
        spotifyClient.getTrack(trackId)?.let { track ->
            printTrackInfo(track)
        }
        
        // Paso 4: Obtener información del álbum
        spotifyClient.getAlbum(albumId)?.let { album ->
            printAlbumInfo(album)
        }
        
        // Paso 5: Obtener información de la playlist
        spotifyClient.getPlaylist(playlistId)?.let { playlist ->
            printPlaylistInfo(playlist)
        }
        
        println("\n" + "=".repeat(70))
        println("✅ TODAS LAS CONSULTAS COMPLETADAS EXITOSAMENTE")
        println("=".repeat(70))
        
    } catch (e: Exception) {
        println("❌ Error general: ${e.message}")
        e.printStackTrace()
    } finally {
        spotifyClient.close()
    }
}