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
import java.io.File
import java.util.*

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
            println("Obteniendo access token")
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
            
            println("Token obtenido exitosamente")
            println("Expires in: ${tokenResponse.expiresIn} seconds")
            true
        } catch (e: Exception) {
            println("Error al obtener token: ${e.message}")
            false
        }
    }
    
    suspend fun getArtist(artistId: String): Artist? {
        return try {
            println("\nObteniendo informacion del artista...")
            val artist: Artist = client.get("https://api.spotify.com/v1/artists/$artistId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }.body()
            
            println("✅ Artista obtenido: ${artist.name}")
            artist
        } catch (e: Exception) {
            println("Error al obtener artista: ${e.message}")
            null
        }
    }
    
    suspend fun getTrack(trackId: String): Track? {
        return try {
            println("\nObteniendo informacion de la cancion...")
            val track: Track = client.get("https://api.spotify.com/v1/tracks/$trackId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }.body()
            
            println("Cancion obtenida: ${track.name}")
            track
        } catch (e: Exception) {
            println("Error al obtener cancion: ${e.message}")
            null
        }
    }
    
    suspend fun getAlbum(albumId: String): Album? {
        return try {
            println("\nObteniendo informacion del album...")
            val album: Album = client.get("https://api.spotify.com/v1/albums/$albumId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }.body()
            
            println("Album obtenido: ${album.name}")
            album
        } catch (e: Exception) {
            println("Error al obtener album: ${e.message}")
            null
        }
    }
    
    suspend fun getPlaylist(playlistId: String): Playlist? {
        return try {
            println("\nObteniendo informacion de la playlist...")
            val playlist: Playlist = client.get("https://api.spotify.com/v1/playlists/$playlistId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }.body()
            
            println("Playlist obtenida: ${playlist.name}")
            playlist
        } catch (e: Exception) {
            println("Error al obtener playlist: ${e.message}")
            null
        }
    }
    
    fun close() {
        client.close()
    }
}
