package org.example

class Autenticacion {

    private val clientId = "43551abad28b4f9290ed67904ee20f5e"   // clave publica de spotify
    private val clientSecret = "dd2408b1ccae4bdca9fd71735f6649eb" // clave secreta de spotify

    suspend fun authenticate(): SpotifyApiClient {
        val spotify = SpotifyApiClient(clientId, clientSecret) // crea el cliente 
        
        require(spotify.authenticate()) {                      // si spotify no acepta 
            "Error: no se pudo autenticar con Spotify"         // error 
        }

        return spotify                                         // se accesio
    }
}
