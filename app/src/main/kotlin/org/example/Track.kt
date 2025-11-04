package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable


@Serializable // marca que esta clase puede convertirse a/desde json
data class Track( // clase que representa una cancion
    val id: String, // id unico de la cancion
    val name: String, // nombre de la cancion
    val artists: List<SimpleArtist>, // lista de artistas de la cancion
    val album: SimpleAlbum, // album al que pertenece
    @SerialName("duration_ms") val durationMs: Long, // duracion en milisegundos
    val explicit: Boolean = false, // indica si tiene contenido explicito, por defecto false
    val popularity: Int = 0, // popularidad de 0 a 100, por defecto 0
    @SerialName("preview_url") val previewUrl: String? = null, // url de preview de 30 segundos, puede ser null
    @SerialName("track_number") val trackNumber: Int = 1, // numero de track en el album, por defecto 1
    @SerialName("disc_number") val discNumber: Int = 1, // numero de disco, por defecto 1
    @SerialName("is_local") val isLocal: Boolean = false, // indica si es archivo local, por defecto false
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null // urls externas, puede ser null
)