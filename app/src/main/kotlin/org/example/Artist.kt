package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable

@Serializable // marca que esta clase puede convertirse a/desde json
data class Artist( // clase que representa un artista
    val id: String, // id unico del artista en spotify
    val name: String, // nombre del artista
    val genres: List<String> = emptyList(), // lista de generos musicales, por defecto vacia
    val popularity: Int = 0, // popularidad de 0 a 100, por defecto 0
    val followers: Followers? = null, // seguidores del artista, puede ser null
    val images: List<Image> = emptyList(), // lista de imagenes del artista, por defecto vacia
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null // urls externas como spotify, puede ser null
)