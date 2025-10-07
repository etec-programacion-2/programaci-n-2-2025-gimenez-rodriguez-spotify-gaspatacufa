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
data class Album(
    val id: String,
    val name: String,
    @SerialName("album_type") val albumType: String,
    val artists: List<SimpleArtist>,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("release_date_precision") val releaseDatePrecision: String,
    @SerialName("total_tracks") val totalTracks: Int,
    val genres: List<String> = emptyList(),
    val label: String? = null,
    val popularity: Int = 0,
    val images: List<Image> = emptyList(),
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null,
    val tracks: TrackList? = null,
    val copyrights: List<Copyright> = emptyList()
)