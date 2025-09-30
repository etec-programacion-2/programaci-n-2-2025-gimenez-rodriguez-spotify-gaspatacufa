package org.example

import io.ktor.client.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import org.example.SpotifyAuthService

class SpotifySearchRepository(
    private val authService: SpotifyAuthService
) : SearchRepository {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private fun getAuthHeader(): String {
        val tokenType = authService.getTokenType()
        val accessToken = authService.getAccessToken()
        return if (tokenType != null && accessToken != null) {
            "$tokenType $accessToken"
        } else {
            ""
        }
    }

    override suspend fun searchTracks(query: String): List<SimpleTrack> {
        if (getAuthHeader().isEmpty()) {
            throw IllegalStateException("No access token available. Authenticate first.")
        }
        return try {
            val response: SearchTracksResponse = client.get("https://api.spotify.com/v1/search") {
                headers {
                    append(HttpHeaders.Authorization, getAuthHeader())
                }
                parameter("q", query)
                parameter("type", "track")
                parameter("limit", "20")
            }.body()

            response.tracks.items
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> {
                    println("Error 401 Unauthorized: Token inválido o expirado.")
                }
                HttpStatusCode.NotFound -> {
                    println("Error 404 Not Found: Recurso no encontrado.")
                }
                else -> {
                    println("Error HTTP: ${e.response.status}")
                }
            }
            emptyList()
        } catch (e: Exception) {
            println("Error buscando tracks: ${e.message}")
            emptyList()
        }
    }

    override suspend fun searchArtists(query: String): List<Artist> {
        if (getAuthHeader().isEmpty()) {
            throw IllegalStateException("No access token available. Authenticate first.")
        }
        return try {
            val response: SearchArtistsResponse = client.get("https://api.spotify.com/v1/search") {
                headers {
                    append(HttpHeaders.Authorization, getAuthHeader())
                }
                parameter("q", query)
                parameter("type", "artist")
                parameter("limit", "20")
            }.body()

            response.artists.items
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> {
                    println("Error 401 Unauthorized: Token inválido o expirado.")
                }
                HttpStatusCode.NotFound -> {
                    println("Error 404 Not Found: Recurso no encontrado.")
                }
                else -> {
                    println("Error HTTP: ${e.response.status}")
                }
            }
            emptyList()
        } catch (e: Exception) {
            println("Error buscando artistas: ${e.message}")
            emptyList()
        }
    }

    fun close() {
        client.close()
    }
}

@Serializable
data class SearchTracksResponse(
    val tracks: TrackList
)

@Serializable
data class SearchArtistsResponse(
    val artists: ArtistList
)

@Serializable
data class ArtistList(
    val items: List<Artist> = emptyList()
)
