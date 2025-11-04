package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable

@Serializable // marca que esta clase puede convertirse a/desde json
data class PlaylistTrackList( // lista paginada de canciones de playlist
    val href: String, // url de la lista
    val limit: Int, // limite de items por pagina
    val next: String? = null, // url de la siguiente pagina, puede ser null
    val offset: Int, // desplazamiento desde el inicio
    val previous: String? = null, // url de la pagina anterior, puede ser null
    val total: Int, // total de items
    val items: List<PlaylistTrackItem> = emptyList() // lista de items de playlist, por defecto vacia
)