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

@Serializable
data class PlaylistOwner(
    val id: String,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null,
    val followers: Followers? = null,
    val images: List<Image> = emptyList()
)