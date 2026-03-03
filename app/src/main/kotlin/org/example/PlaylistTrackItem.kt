package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable

@Serializable // marca que esta clase puede convertirse a/desde json
data class PlaylistTrackItem( // item que contiene una cancion en una playlist
    @SerialName("added_at") val addedAt: String? = null, // fecha cuando se agrego, puede ser null
    @SerialName("added_by") val addedBy: PlaylistOwner? = null, // quien la agrego, puede ser null
    @SerialName("is_local") val isLocal: Boolean = false, // indica si es archivo local, por defecto false
    val track: PlaylistTrack? = null // la cancion, puede ser null
)