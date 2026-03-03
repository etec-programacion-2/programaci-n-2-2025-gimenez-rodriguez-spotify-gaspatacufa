package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable

@Serializable // marca que esta clase puede convertirse a/desde json
data class PlaylistTrack( // cancion dentro de una playlist
    val id: String? = null, // id de la cancion, puede ser null
    val name: String, // nombre de la cancion
    val artists: List<SimpleArtist>, // lista de artistas
    val album: SimpleAlbum? = null, // album al que pertenece, puede ser null
    @SerialName("duration_ms") val durationMs: Long, // duracion en milisegundos
    val explicit: Boolean = false, // indica si tiene contenido explicito, por defecto false
    val popularity: Int = 0, // popularidad de 0 a 100, por defecto 0
    @SerialName("preview_url") val previewUrl: String? = null, // url de preview, puede ser null
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null, // urls externas, puede ser null
    @SerialName("is_local") val isLocal: Boolean = false // indica si es archivo local, por defecto false
)