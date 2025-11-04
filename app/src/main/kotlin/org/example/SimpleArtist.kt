package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable

@Serializable // marca que esta clase puede convertirse a/desde json
data class SimpleArtist( // version simplificada de artista
    val id: String, // id unico del artista
    val name: String, // nombre del artista
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null // urls externas, puede ser null
)