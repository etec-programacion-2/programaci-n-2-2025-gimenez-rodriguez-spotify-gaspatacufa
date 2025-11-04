package org.example

import kotlinx.serialization.SerialName // importa anotacion para mapear nombres json
import kotlinx.serialization.Serializable // importa anotacion para hacer la clase serializable

@Serializable // marca que esta clase puede convertirse a/desde json
data class TokenResponse( // respuesta del endpoint de autenticacion
    @SerialName("access_token") val accessToken: String, // token de acceso
    @SerialName("token_type") val tokenType: String, // tipo de token, usualmente Bearer
    @SerialName("expires_in") val expiresIn: Int // tiempo en segundos hasta que expire el token
)