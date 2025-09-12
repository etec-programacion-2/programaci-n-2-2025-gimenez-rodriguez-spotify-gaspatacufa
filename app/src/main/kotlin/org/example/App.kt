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
        
        val artistId = "06HL4z0CvFAxyc27GXpf02" // ID de Bad Bunny
        val url = "https://api.spotify.com/v1/artists/$artistId"
        println("URL de la solicitud: $url")

        val artist: Artist = client.get(url) {
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