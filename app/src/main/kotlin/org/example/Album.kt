package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable



@Serializable // marca que esta clase puede convertirse a/desde json
data class Album( // clase que representa un album
    val id: String, // id unico del album
    val name: String, // nombre del album
    @SerialName("album_type") val albumType: String, // tipo de album: single, album o compilation
    val artists: List<SimpleArtist>, // lista de artistas del album
    @SerialName("release_date") val releaseDate: String, // fecha de lanzamiento
    @SerialName("release_date_precision") val releaseDatePrecision: String, // precision de la fecha: year, month o day
    @SerialName("total_tracks") val totalTracks: Int, // cantidad total de canciones
    val genres: List<String> = emptyList(), // lista de generos, por defecto vacia
    val label: String? = null, // sello discografico, puede ser null
    val popularity: Int = 0, // popularidad de 0 a 100, por defecto 0
    val images: List<Image> = emptyList(), // lista de imagenes, por defecto vacia
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null, // urls externas, puede ser null
    val tracks: TrackList? = null, // lista de canciones del album, puede ser null
    val copyrights: List<Copyright> = emptyList() // informacion de derechos de autor, por defecto vacia
)