package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable

@Serializable // marca que esta clase puede convertirse a/desde json
data class Image( // imagen de un artista, album, playlist, etc
    val url: String, // url de la imagen
    val height: Int? = null, // alto en pixeles, puede ser null
    val width: Int? = null // ancho en pixeles, puede ser null
)