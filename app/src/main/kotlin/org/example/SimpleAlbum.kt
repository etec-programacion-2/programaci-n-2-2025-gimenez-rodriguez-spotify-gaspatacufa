package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable

@Serializable // marca que esta clase puede convertirse a/desde json
data class SimpleAlbum( // version simplificada de album
    val id: String, // id unico del album
    val name: String, // nombre del album
    @SerialName("album_type") val albumType: String, // tipo de album
    val artists: List<SimpleArtist>, // lista de artistas
    @SerialName("release_date") val releaseDate: String? = null, // fecha de lanzamiento, puede ser null
    @SerialName("total_tracks") val totalTracks: Int = 0, // cantidad de canciones, por defecto 0
    val images: List<Image> = emptyList(), // lista de imagenes, por defecto vacia
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null // urls externas, puede ser null
)