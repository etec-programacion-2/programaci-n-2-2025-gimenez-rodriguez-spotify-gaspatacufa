package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import java.util.*

class SpotifyAuthService(
    private val clientId: String,
    private val clientSecret: String
) {
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
            true
        } catch (e: Exception) {
            println("Error authenticating: ${e.message}")
            false
        }
    }

    fun getAccessToken(): String? = accessToken
    fun getTokenType(): String? = tokenType

    fun close() {
        client.close()
    }
}

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int
)
