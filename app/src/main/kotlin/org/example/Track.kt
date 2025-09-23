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
data class Track(
    val id: String,
    val name: String,
    val artists: List<SimpleArtist>,
    val album: SimpleAlbum,
    @SerialName("duration_ms") val durationMs: Long,
    val explicit: Boolean = false,
    val popularity: Int = 0,
    @SerialName("preview_url") val previewUrl: String? = null,
    @SerialName("track_number") val trackNumber: Int = 1,
    @SerialName("disc_number") val discNumber: Int = 1,
    @SerialName("is_local") val isLocal: Boolean = false,
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null
)