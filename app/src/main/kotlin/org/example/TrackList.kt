package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable


@Serializable // marca que esta clase puede convertirse a/desde json
data class TrackList( // lista paginada de canciones
    val href: String, // url de la lista
    val limit: Int, // limite de items por pagina
    val next: String? = null, // url de la siguiente pagina, puede ser null
    val offset: Int, // desplazamiento desde el inicio
    val previous: String? = null, // url de la pagina anterior, puede ser null
    val total: Int, // total de items
    val items: List<SimpleTrack> = emptyList() // lista de canciones, por defecto vacia
)