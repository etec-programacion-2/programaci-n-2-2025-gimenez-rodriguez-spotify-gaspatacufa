package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Interfaz para repositorio específico de tracks.
 * Proporciona operaciones más detalladas sobre tracks individuales.
 */
interface TrackRepository {
    /**
     * Obtiene un track específico por su ID
     * @param trackId ID del track en Spotify
     * @return Track encontrado
     * @throws SpotifyApiException en caso de error
     */
    suspend fun getTrackById(trackId: String): Track
    
    /**
     * Obtiene múltiples tracks por sus IDs
     * @param trackIds Lista de IDs de tracks (máximo 50)
     * @return Lista de tracks encontrados
     * @throws SpotifyApiException en caso de error
     */
    suspend fun getTracksByIds(trackIds: List<String>): List<Track>
    
    /**
     * Obtiene tracks populares de un artista
     * @param artistId ID del artista
     * @param market Código de país (opcional, por defecto "US")
     * @return Lista de tracks populares
     * @throws SpotifyApiException en caso de error
     */
    suspend fun getArtistTopTracks(artistId: String, market: String = "US"): List<Track>
}

// ============================================================================
// MODELOS DE RESPUESTA ESPECÍFICOS PARA TRACKS
// ============================================================================

@Serializable
data class MultipleTracksResponse(
    val tracks: List<Track?> = emptyList()
)

@Serializable
data class ArtistTopTracksResponse(
    val tracks: List<Track> = emptyList()
)

/**
 * Implementación específica para operaciones de tracks usando la API de Spotify.
 * Demuestra Inyección de Dependencias recibiendo AuthService en el constructor.
 */
class SpotifyTrackRepository(
    private val authService: AuthService
) : TrackRepository {
    
    companion object {
        private const val BASE_URL = "https://api.spotify.com/v1"
        private const val TRACKS_ENDPOINT = "$BASE_URL/tracks"
        private const val ARTISTS_ENDPOINT = "$BASE_URL/artists"
    }
    
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    
    override suspend fun getTrackById(trackId: String): Track {
        return try {
            val token = authService.getValidAccessToken()
            val url = "$TRACKS_ENDPOINT/$trackId"
            
            println("🎵 Obteniendo track por ID: $trackId")
            
            val track: Track = client.get(url) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body()
            
            println("✅ Track obtenido: ${track.name}")
            track
            
        } catch (e: Exception) {
            when (e) {
                is SpotifyApiException -> throw e
                else -> {
                    println("❌ Error al obtener track: ${e.message}")
                    throw SpotifyApiException("Error al obtener track con ID: $trackId", e)
                }
            }
        }
    }
    
    override suspend fun getTracksByIds(trackIds: List<String>): List<Track> {
        if (trackIds.isEmpty()) return emptyList()
        
        return try {
            val token = authService.getValidAccessToken()
            val ids = trackIds.take(50).joinToString(",") // Spotify permite máximo 50 IDs
            val url = "$TRACKS_ENDPOINT?ids=$ids"
            
            println("🎵 Obteniendo múltiples tracks: ${trackIds.size} tracks")
            
            val response: MultipleTracksResponse = client.get(url) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body()
            
            val validTracks = response.tracks.filterNotNull()
            println("✅ Tracks obtenidos: ${validTracks.size} de ${trackIds.size}")
            validTracks
            
        } catch (e: Exception) {
            when (e) {
                is SpotifyApiException -> throw e
                else -> {
                    println("❌ Error al obtener tracks múltiples: ${e.message}")
                    throw SpotifyApiException("Error al obtener múltiples tracks", e)
                }
            }
        }
    }
    
    override suspend fun getArtistTopTracks(artistId: String, market: String): List<Track> {
        return try {
            val token = authService.getValidAccessToken()
            val url = "$ARTISTS_ENDPOINT/$artistId/top-tracks?market=$market"
            
            println("🎤 Obteniendo top tracks del artista: $artistId")
            
            val response: ArtistTopTracksResponse = client.get(url) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body()
            
            println("✅ Top tracks obtenidos: ${response.tracks.size} tracks")
            response.tracks
            
        } catch (e: Exception) {
            when (e) {
                is SpotifyApiException -> throw e
                else -> {
                    println("❌ Error al obtener top tracks: ${e.message}")
                    throw SpotifyApiException("Error al obtener top tracks del artista: $artistId", e)
                }
            }
        }
    }
    
    fun close() {
        client.close()
    }
}