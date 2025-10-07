package org.example

data class Ejecutar(
    val clientId: String,
    val clientSecret: String,
    val archivoArtistas: String,
    val archivoPistas: String,
    val archivoAlbumes: String,
    val archivoPlaylists: String
) {
    suspend fun ejecutar() {
        val artistaSeleccionado = seleccionarIdAleatorio(archivoArtistas) ?: "ID predeterminado"
        val pistaSeleccionada = seleccionarIdAleatorio(archivoPistas) ?: "ID predeterminado"
        val albumSeleccionado = seleccionarIdAleatorio(archivoAlbumes) ?: "ID predeterminado"
        val playlistSeleccionada = seleccionarIdAleatorio(archivoPlaylists) ?: "ID predeterminado"

        println("Artista seleccionado: $artistaSeleccionado")
        println("Pista seleccionada: $pistaSeleccionada")
        println("Album seleccionado: $albumSeleccionado")
        println("Playlist seleccionada: $playlistSeleccionada")
        
        val spotifyClient = SpotifyApiClient(clientId, clientSecret)

        try {
            if (!spotifyClient.authenticate()) {
                println("No se pudo obtener el token de acceso")
                return
            }

            println("\n" + "=".repeat(70))
            println("INICIANDO CONSULTAS A LA API DE SPOTIFY")
            println("=".repeat(70))

            spotifyClient.getArtist(artistaSeleccionado)?.let { printArtistInfo(it) }
            spotifyClient.getTrack(pistaSeleccionada)?.let { printTrackInfo(it) }
            spotifyClient.getAlbum(albumSeleccionado)?.let { printAlbumInfo(it) }
            spotifyClient.getPlaylist(playlistSeleccionada)?.let { printPlaylistInfo(it) }

            println("\n" + "=".repeat(70))
            println("TODAS LAS CONSULTAS COMPLETADAS EXITOSAMENTE")
            println("=".repeat(70))

        } catch (e: Exception) {
            println("❌ Error general: ${e.message}")
            e.printStackTrace()
        } finally {
            spotifyClient.close()
        }
    }
}