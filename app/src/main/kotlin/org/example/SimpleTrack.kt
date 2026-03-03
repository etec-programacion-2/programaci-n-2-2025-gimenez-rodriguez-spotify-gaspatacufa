package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable

@Serializable // marca que esta clase puede convertirse a/desde json
data class SimpleTrack( // version simplificada de cancion
    val id: String, // id unico de la cancion
    val name: String, // nombre de la cancion
    val artists: List<SimpleArtist>, // lista de artistas
    @SerialName("duration_ms") val durationMs: Long, // duracion en milisegundos
    val explicit: Boolean = false, // indica si tiene contenido explicito, por defecto false
    @SerialName("track_number") val trackNumber: Int, // numero de track
    @SerialName("disc_number") val discNumber: Int = 1, // numero de disco, por defecto 1
    @SerialName("preview_url") val previewUrl: String? = null, // url de preview, puede ser null
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null // urls externas, puede ser null
)