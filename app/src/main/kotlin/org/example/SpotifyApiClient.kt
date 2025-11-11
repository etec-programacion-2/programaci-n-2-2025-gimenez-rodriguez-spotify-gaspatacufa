package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.*

// cliente para interactuar con la api de spotify
class SpotifyApiClient(val clientId: String, val clientSecret: String) {
    
    private val jsonParser = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
    
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(jsonParser)
        }
    }
    
    private var accessToken: String? = null
    private var tokenType: String? = null
    
    // obtiene el token de acceso de spotify
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
            
            println("✅ Token obtenido exitosamente")
            println("Expires in: ${tokenResponse.expiresIn} seconds")
            true
        } catch (e: Exception) {
            println("❌ Error al obtener token: ${e.message}")
            false
        }
    }
    
    // obtiene info de un artista desde spotify
    suspend fun getArtist(artistId: String): Artist? {
        return try {
            println("\n🔍 Obteniendo informacion del artista...")
            println("   ID solicitado: '$artistId'")
            
            val response: HttpResponse = client.get("https://api.spotify.com/v1/artists/$artistId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }
            
            val responseBody = response.bodyAsText()
            println("   Status: ${response.status.value} ${response.status.description}")
            println("   Respuesta: ${responseBody.take(300)}")
            
            if (response.status.isSuccess()) {
                val artist = jsonParser.decodeFromString<Artist>(responseBody)
                println("✅ Artista obtenido: ${artist.name}")
                artist
            } else {
                println("❌ Error HTTP ${response.status.value}")
                null
            }
        } catch (e: Exception) {
            println("❌ Error: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    // obtiene info de una cancion desde spotify
    suspend fun getTrack(trackId: String): Track? {
        return try {
            println("\n🔍 Obteniendo informacion de la cancion...")
            println("   ID solicitado: '$trackId'")
            
            val response: HttpResponse = client.get("https://api.spotify.com/v1/tracks/$trackId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }
            
            val responseBody = response.bodyAsText()
            println("   Status: ${response.status.value} ${response.status.description}")
            println("   Respuesta: ${responseBody.take(300)}")
            
            if (response.status.isSuccess()) {
                val track = jsonParser.decodeFromString<Track>(responseBody)
                println("✅ Cancion obtenida: ${track.name}")
                track
            } else {
                println("❌ Error HTTP ${response.status.value}")
                null
            }
        } catch (e: Exception) {
            println("❌ Error: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    // obtiene info de un album desde spotify
    suspend fun getAlbum(albumId: String): Album? {
        return try {
            println("\n🔍 Obteniendo informacion del album...")
            println("   ID solicitado: '$albumId'")
            
            val response: HttpResponse = client.get("https://api.spotify.com/v1/albums/$albumId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }
            
            val responseBody = response.bodyAsText()
            println("   Status: ${response.status.value} ${response.status.description}")
            println("   Respuesta: ${responseBody.take(300)}")
            
            if (response.status.isSuccess()) {
                val album = jsonParser.decodeFromString<Album>(responseBody)
                println("✅ Album obtenido: ${album.name}")
                album
            } else {
                println("❌ Error HTTP ${response.status.value}")
                null
            }
        } catch (e: Exception) {
            println("❌ Error: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    // obtiene info de una playlist desde spotify
    suspend fun getPlaylist(playlistId: String): Playlist? {
        return try {
            println("\n🔍 Obteniendo informacion de la playlist...")
            println("   ID solicitado: '$playlistId'")
            
            val response: HttpResponse = client.get("https://api.spotify.com/v1/playlists/$playlistId") {
                headers {
                    append(HttpHeaders.Authorization, "$tokenType $accessToken")
                }
            }
            
            val responseBody = response.bodyAsText()
            println("   Status: ${response.status.value} ${response.status.description}")
            
            if (response.status.isSuccess()) {
                val playlist = jsonParser.decodeFromString<Playlist>(responseBody)
                println("✅ Playlist obtenida: ${playlist.name}")
                playlist
            } else {
                println("❌ Error HTTP ${response.status.value}")
                println("   Respuesta: ${responseBody.take(300)}")
                null
            }
        } catch (e: Exception) {
            println("❌ Error: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    fun close() {
        client.close()
    }
}