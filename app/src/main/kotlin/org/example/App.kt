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
data class Artist(
    val id: String,
    val name: String,
    val genres: List<String> = emptyList(),
    val popularity: Int = 0,
    val followers: Followers? = null
)

@Serializable
data class Followers(
    val total: Int
)

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int
)

suspend fun main() {
    // ⚠️ REEMPLAZA CON TUS CREDENCIALES REALES
    val clientId = "43551abad28b4f9290ed67904ee20f5e"
    val clientSecret = "dd2408b1ccae4bdca9fd71735f6649eb"
    
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    
    try {
        // Paso 1: Obtener el access token
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
        
        println("✅ Token obtenido exitosamente")
        println("Token type: ${tokenResponse.tokenType}")
        println("Expires in: ${tokenResponse.expiresIn} seconds")
        
        // Paso 2: Usar el token para obtener información del artista
        println("\n🎵 Obteniendo información del artista...")
        
        val artist: Artist = client.get("https://api.spotify.com/v1/artists/0TnOYISbd1XYRBk9myaseg") {
            headers {
                append(HttpHeaders.Authorization, "${tokenResponse.tokenType} ${tokenResponse.accessToken}")
            }
        }.body()
        
        println("✅ Artista obtenido exitosamente:")
        println("ID: ${artist.id}")
        println("Name: ${artist.name}")
        println("Genres: ${artist.genres.joinToString(", ")}")
        println("Popularity: ${artist.popularity}")
        println("Followers: ${artist.followers?.total ?: "N/A"}")
        
    } catch (e: Exception) {
        println("❌ Error: ${e.message}")
        e.printStackTrace()
    } finally {
        client.close()
    }
}