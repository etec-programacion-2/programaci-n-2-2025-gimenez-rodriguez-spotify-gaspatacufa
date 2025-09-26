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
import java.net.URLEncoder

/**
 * Interfaz principal para operaciones de búsqueda en Spotify.
 * Esta interfaz define el contrato que debe cumplir cualquier implementación
 * de repositorio de búsqueda, permitiendo cambiar la implementación sin
 * afectar el código cliente (Principio de Inversión de Dependencias).
 */
interface SearchRepository {
    /**
     * Busca tracks por consulta de texto
     * @param query Términos de búsqueda
     * @param limit Número máximo de resultados (por defecto 20)
     * @return Lista de tracks encontrados
     * @throws SpotifyApiException en caso de error
     */
    suspend fun searchTracks(query: String, limit: Int = 20): List<Track>
    
    /**
     * Busca artistas por consulta de texto
     * @param query Términos de búsqueda
     * @param limit Número máximo de resultados (por defecto 20)
     * @return Lista de artistas encontrados
     * @throws SpotifyApiException en caso de error
     */
    suspend fun searchArtists(query: String, limit: Int = 20): List<Artist>
    
    /**
     * Busca álbumes por consulta de texto
     * @param query Términos de búsqueda
     * @param limit Número máximo de resultados (por defecto 20)
     * @return Lista de álbumes encontrados
     * @throws SpotifyApiException en caso de error
     */
    suspend fun searchAlbums(query: String, limit: Int = 20): List<Album>
}

// ============================================================================
// MODELOS DE RESPUESTA PARA BÚSQUEDA
// ============================================================================

@Serializable
data class SpotifySearchResponse(
    val tracks: SpotifyTracksSearchResponse? = null,
    val artists: SpotifyArtistsSearchResponse? = null,
    val albums: SpotifyAlbumsSearchResponse? = null
)

@Serializable
data class SpotifyTracksSearchResponse(
    val items: List<Track> = emptyList(),
    val total: Int = 0,
    val limit: Int = 20,
    val offset: Int = 0
)

@Serializable
data class SpotifyArtistsSearchResponse(
    val items: List<Artist> = emptyList(),
    val total: Int = 0,
    val limit: Int = 20,
    val offset: Int = 0
)

@Serializable
data class SpotifyAlbumsSearchResponse(
    val items: List<Album> = emptyList(),
    val total: Int = 0,
    val limit: Int = 20,
    val offset: Int = 0
)

/**
 * Implementación del repositorio de búsqueda que utiliza la API de Spotify.
 * Esta clase demuestra la Inyección de Dependencias recibiendo AuthService
 * en su constructor.
 */
class SpotifySearchRepository(
    private val authService: AuthService
) : SearchRepository {
    
    companion object {
        private const val BASE_URL = "https://api.spotify.com/v1"
        private const val SEARCH_ENDPOINT = "$BASE_URL/search"
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
    
    override suspend fun searchTracks(query: String, limit: Int): List<Track> {
        return try {
            val token = authService.getValidAccessToken()
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val url = "$SEARCH_ENDPOINT?q=$encodedQuery&type=track&limit=$limit"
            
            val response: SpotifySearchResponse = client.get(url) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body()
            
            response.tracks?.items ?: emptyList()
            
        } catch (e: Exception) {
            when (e) {
                is SpotifyApiException -> throw e
                else -> {
                    println("❌ Error al buscar tracks: ${e.message}")
                    throw SpotifyApiException("Error al buscar tracks", e)
                }
            }
        }
    }
    
    override suspend fun searchArtists(query: String, limit: Int): List<Artist> {
        return try {
            val token = authService.getValidAccessToken()
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val url = "$SEARCH_ENDPOINT?q=$encodedQuery&type=artist&limit=$limit"
            
            val response: SpotifySearchResponse = client.get(url) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body()
            
            response.artists?.items ?: emptyList()
            
        } catch (e: Exception) {
            when (e) {
                is SpotifyApiException -> throw e
                else -> {
                    println("❌ Error al buscar artistas: ${e.message}")
                    throw SpotifyApiException("Error al buscar artistas", e)
                }
            }
        }
    }
    
    override suspend fun searchAlbums(query: String, limit: Int): List<Album> {
        return try {
            val token = authService.getValidAccessToken()
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val url = "$SEARCH_ENDPOINT?q=$encodedQuery&type=album&limit=$limit"
            
            val response: SpotifySearchResponse = client.get(url) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body()
            
            response.albums?.items ?: emptyList()
            
        } catch (e: Exception) {
            when (e) {
                is SpotifyApiException -> throw e
                else -> {
                    println("❌ Error al buscar álbumes: ${e.message}")
                    throw SpotifyApiException("Error al buscar álbumes", e)
                }
            }
        }
    }
    
    fun close() {
        client.close()
    }
}