package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.util.*

/**
 * Interfaz para el servicio de autenticación de Spotify.
 * Define el contrato para obtener y gestionar tokens de acceso.
 */
interface AuthService {
    suspend fun getValidAccessToken(): String
    suspend fun authenticate(): Boolean
    fun isTokenValid(): Boolean
}

/**
 * Implementación del servicio de autenticación para Spotify.
 * Maneja la obtención y renovación automática de tokens de acceso.
 */
class SpotifyAuthService(
    private val clientId: String,
    private val clientSecret: String
) : AuthService {
    
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
    private var tokenType: String = "Bearer"
    private var tokenExpirationTime: Long = 0L
    
    /**
     * Obtiene un token de acceso válido, renovándolo si es necesario
     */
    override suspend fun getValidAccessToken(): String {
        if (!isTokenValid()) {
            if (!authenticate()) {
                throw SpotifyApiException("No se pudo obtener un token de acceso válido")
            }
        }
        return accessToken ?: throw SpotifyApiException("Token de acceso no disponible")
    }
    
    /**
     * Autentica con Spotify y obtiene un nuevo token
     */
    override suspend fun authenticate(): Boolean {
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
            // Establecer tiempo de expiración con un margen de seguridad de 5 minutos
            tokenExpirationTime = System.currentTimeMillis() + (tokenResponse.expiresIn - 300) * 1000
            
            println("✅ Token obtenido exitosamente")
            true
        } catch (e: Exception) {
            println("❌ Error al obtener token: ${e.message}")
            false
        }
    }
    
    /**
     * Verifica si el token actual es válido y no ha expirado
     */
    override fun isTokenValid(): Boolean {
        return accessToken != null && System.currentTimeMillis() < tokenExpirationTime
    }
    
    fun close() {
        client.close()
    }
}

/**
 * Excepción personalizada para errores de la API de Spotify
 */
class SpotifyApiException(message: String, cause: Throwable? = null) : Exception(message, cause)