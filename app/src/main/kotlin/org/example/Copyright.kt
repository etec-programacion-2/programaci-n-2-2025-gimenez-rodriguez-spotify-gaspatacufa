package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable

@Serializable // marca que esta clase puede convertirse a/desde json
data class Copyright( // informacion de derechos de autor
    val text: String, // texto del copyright
    val type: String )
                        // tipo de copyright: C o P