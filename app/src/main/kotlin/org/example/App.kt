package org.example





import io.ktor.client.statement.*
import kotlinx.coroutines.*
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
    
    suspend fun authenticate(): Any {
        return try {
            println("🔑 Obteniendo access token...")
            val credentials = Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())
            
            val tokenResponse: HttpResponse = client.submitForm(
                url = "https://accounts.spotify.com/api/token",
                formParameters = parameters {
                    append("grant_type", "client_credentials")
                }
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Basic $credentials")
                    append(HttpHeaders.ContentType, "application/x-www-form-urlencoded")
                }
            } else  {
                println("❌ No se encontraron artistas para: '$query'")
            }
        } catch (e: SpotifyApiException) {
            println("❌ Error al buscar artistas: ${e.message}")
        }
    }
    
    /**
     * Busca y muestra información de álbumes
     */
    suspend fun searchAndDisplayAlbums(query: String, limit: Int = 5) {
        println("\n🔍 Buscando álbumes: '$query'")
        try {
            val albums = searchRepository.searchAlbums(query, limit)
            if (albums.isNotEmpty()) {
                println("✅ Encontrados ${albums.size} álbumes:")
                albums.forEach { album ->
                    println("  • ${album.name} - ${album.artists.joinToString(", ") { it.name }} (${album.releaseDate})")
                }
            } else {
                println("❌ No se encontraron álbumes para: '$query'")
            }
        } catch (e: SpotifyApiException) {
            println("❌ Error al buscar álbumes: ${e.message}")
        }
    }
    
    /**
     * Obtiene y muestra información detallada de un track específico
     */
    suspend fun getAndDisplayTrackDetails(trackId: String) {
        println("\n🎵 Obteniendo detalles del track ID: $trackId")
        try {
            val track = trackRepository.getTrackById(trackId)
            printDetailedTrackInfo(track)
        } catch (e: SpotifyApiException) {
            println("❌ Error al obtener track: ${e.message}")
        }
    }
    
    /**
     * Obtiene y muestra los top tracks de un artista
     */
    suspend fun getAndDisplayArtistTopTracks(artistId: String) {
        println("\n🎤 Obteniendo top tracks del artista ID: $artistId")
        try {
            val topTracks = trackRepository.getArtistTopTracks(artistId)
            if (topTracks.isNotEmpty()) {
                println("✅ Top tracks:")
                topTracks.forEachIndexed { index, track ->
                    println("  ${index + 1}. ${track.name} - Popularidad: ${track.popularity}/100")
                }
            } else {
                println("❌ No se encontraron top tracks para el artista")
            }
        } catch (e: SpotifyApiException) {
            println("❌ Error al obtener top tracks: ${e.message}")
        }
    }
    
    private fun printDetailedTrackInfo(track: Track) {
        println("\n" + "=".repeat(50))
        println("🎵 DETALLES DEL TRACK")
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
        println("=".repeat(50))
    }
}

suspend fun main() {
    val clientId = "43551abad28b4f9290ed67904ee20f5e"
    val clientSecret = "dd2408b1ccae4bdca9fd71735f6649eb"

    val authService = SpotifyAuthService(clientId, clientSecret)
    if (!authService.authenticate()) {
        println("❌ No se pudo obtener el token de acceso")
        return
    }

    val searchRepository: SearchRepository = SpotifySearchRepository(authService)

    // Ejemplo de búsqueda de tracks
    val tracks = searchRepository.searchTracks("Imagine Dragons")
    println("Tracks encontrados: ${tracks.size}")
    tracks.forEach { track ->
        println("- ${track.name} por ${track.artists.joinToString { it.name }}")
    }

    // Ejemplo de búsqueda de artistas
    val artists = searchRepository.searchArtists("Adele")
    println("Artistas encontrados: ${artists.size}")
    artists.forEach { artist ->
        println("- ${artist.name}")
    }

    // Cerrar clientes HTTP
    (searchRepository as? SpotifySearchRepository)?.close()
    authService.close()
}
