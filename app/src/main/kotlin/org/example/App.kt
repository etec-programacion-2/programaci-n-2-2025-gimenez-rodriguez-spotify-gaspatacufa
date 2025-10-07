package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*

suspend fun main() {
    val app = Ejecutar(
        clientId = "43551abad28b4f9290ed67904ee20f5e",
        clientSecret = "dd2408b1ccae4bdca9fd71735f6649eb",
        archivoArtistas = "D:/programaci-n-2-2025-gimenez-rodriguez-spotify-gaspatacufa/data/artistas.txt",
        archivoAlbumes = "D:/programaci-n-2-2025-gimenez-rodriguez-spotify-gaspatacufa/data/albumes.txt",
        archivoPistas = "D:/programaci-n-2-2025-gimenez-rodriguez-spotify-gaspatacufa/data/pistas.txt",
        archivoPlaylists = "D:/programaci-n-2-2025-gimenez-rodriguez-spotify-gaspatacufa/data/playlists.txt"

    )
    app.ejecutar()
}