package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable

@Serializable // marca que esta clase puede convertirse a/desde json
data class Playlist( // clase que representa una playlist
    val id: String, // id unico de la playlist
    val name: String, // nombre de la playlist
    val description: String? = null, // descripcion de la playlist, puede ser null
    val owner: PlaylistOwner, // dueño de la playlist
    val public: Boolean? = null, // indica si es publica, puede ser null
    val collaborative: Boolean = false, // indica si es colaborativa, por defecto false
    val followers: Followers, // seguidores de la playlist
    val images: List<Image> = emptyList(), // lista de imagenes, por defecto vacia
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null, // urls externas, puede ser null
    @SerialName("snapshot_id") val snapshotId: String, // id de la version actual de la playlist
    val tracks: PlaylistTrackList // lista de canciones de la playlist
)
