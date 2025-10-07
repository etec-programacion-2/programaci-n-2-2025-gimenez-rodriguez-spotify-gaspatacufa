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
    val followers: Followers? = null,
    val images: List<Image> = emptyList(),
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null
)