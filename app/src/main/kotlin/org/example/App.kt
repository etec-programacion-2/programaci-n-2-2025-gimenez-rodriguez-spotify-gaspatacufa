package org.example

suspend fun main(args: Array<String>) {
    val clientId = "43551abad28b4f9290ed67904ee20f5e"
    val clientSecret = "dd2408b1ccae4bdca9fd71735f6649eb"
    
    val spotifyClient = SpotifyApiClient(clientId, clientSecret)
    
    if (!spotifyClient.authenticate()) {
        println("No se pudo obtener el token de acceso")
        return
    }
    
    if (args.contains("--web")) {
        startWebServer(spotifyClient)
    } else {
        val app = Ejecutar(
            clientId = clientId,
            clientSecret = clientSecret,
            archivoArtistas = "D:\\programaci-n-2-2025-gimenez-rodriguez-spotify-gaspatacufa\\data\\artistas.txt",
            archivoAlbumes = "D:\\programaci-n-2-2025-gimenez-rodriguez-spotify-gaspatacufa\\data\\albumes.txt",
            archivoPistas = "D:\\programaci-n-2-2025-gimenez-rodriguez-spotify-gaspatacufa\\data\\pistas.txt",
            archivoPlaylists = "D:\\programaci-n-2-2025-gimenez-rodriguez-spotify-gaspatacufa\\data\\playlists.txt"
        )
        app.ejecutar()
    }
}