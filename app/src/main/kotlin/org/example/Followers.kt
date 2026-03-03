package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable*

@Serializable // marca que esta clase puede convertirse a/desde json
data class Followers( // seguidores de un artista, playlist o usuario
    val total: Int )  
                        //cantidad total de seguidores