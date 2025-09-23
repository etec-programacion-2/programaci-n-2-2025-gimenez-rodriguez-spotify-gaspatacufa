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
data class Playlist(
    val id: String,
    val name: String,
    val description: String? = null,
    val owner: PlaylistOwner,
    val public: Boolean? = null,
    val collaborative: Boolean = false,
    val followers: Followers,
    val images: List<Image> = emptyList(),
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null,
    @SerialName("snapshot_id") val snapshotId: String,
    val tracks: PlaylistTrackList
)
