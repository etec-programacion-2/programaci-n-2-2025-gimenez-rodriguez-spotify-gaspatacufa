package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable*

@Serializable // marca que esta clase puede convertirse a/desde json
data class PlaylistOwner( // dueño de una playlist
    val id: String, // id del usuario
    @SerialName("display_name") val displayName: String? = null, // nombre para mostrar, puede ser null
    @SerialName("external_urls") val externalUrls: ExternalUrls? = null, // urls externas, puede ser null
    val followers: Followers? = null, // seguidores del usuario, puede ser null
    val images: List<Image> = emptyList() // lista de imagenes, por defecto vacia
)