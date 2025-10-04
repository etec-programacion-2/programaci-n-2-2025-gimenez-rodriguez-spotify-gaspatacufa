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
data class PlaylistTrack(
    val id: String? = null,
    val name: String,
    val artists: List<SimpleArtist>,
    val album: SimpleAlbum? = null,
    @SerialName("duration_ms") val durationMs: Long,
    val explicit: Boolean = false,
    val popularity: Int = 0,
    @SerialName("preview_url") val previewUrl: String? = null,
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null,
    @SerialName("is_local") val isLocal: Boolean = false
)