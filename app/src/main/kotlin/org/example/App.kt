package org.example

/**
 * Clase que demuestra el uso de la capa de abstracción sobre la API de Spotify.
 * Implementa el principio de Inversión de Dependencias programando contra interfaces,
 * no contra implementaciones concretas.
 */
class SpotifyService(
    private val searchRepository: SearchRepository,
    private val trackRepository: TrackRepository
) {
    
    /**
     * Busca y muestra información de tracks
     */
    suspend fun searchAndDisplayTracks(query: String, limit: Int = 5) {
        println("\n🔍 Buscando tracks: '$query'")
        try {
            val tracks = searchRepository.searchTracks(query, limit)
            if (tracks.isNotEmpty()) {
                println("✅ Encontrados ${tracks.size} tracks:")
                tracks.forEach { track ->
                    println("  • ${track.name} - ${track.artists.joinToString(", ") { it.name }}")
                }
            } else {
                println("❌ No se encontraron tracks para: '$query'")
            }
        } catch (e: SpotifyApiException) {
            println("❌ Error al buscar tracks: ${e.message}")
        }
    }
    
    /**
     * Busca y muestra información de artistas
     */
    suspend fun searchAndDisplayArtists(query: String, limit: Int = 5) {
        println("\n🔍 Buscando artistas: '$query'")
        try {
            val artists = searchRepository.searchArtists(query, limit)
            if (artists.isNotEmpty()) {
                println("✅ Encontrados ${artists.size} artistas:")
                artists.forEach { artist ->
                    val genres = if (artist.genres.isNotEmpty()) 
                        " (${artist.genres.take(2).joinToString(", ")})" 
                    else ""
                    println("  • ${artist.name}$genres - Popularidad: ${artist.popularity}/100")
                }
            } else {
                println("❌ No se encontraron artistas para: '$query'")
            }
        } catch (e: SpotifyApiException) {
            println("❌ Error al buscar artistas: ${e.message}")
        }
    }
    
    /**
     * Busca y muestra información de álbumes
     */
    suspend fun searchAndDisplayAlbums(query: String, limit: Int = 5) {
        println("\n🔍 Buscando álbumes: '$query'")
        try {
            val albums = searchRepository.searchAlbums(query, limit)
            if (albums.isNotEmpty()) {
                println("✅ Encontrados ${albums.size} álbumes:")
                albums.forEach { album ->
                    println("  • ${album.name} - ${album.artists.joinToString(", ") { it.name }} (${album.releaseDate})")
                }
            } else {
                println("❌ No se encontraron álbumes para: '$query'")
            }
        } catch (e: SpotifyApiException) {
            println("❌ Error al buscar álbumes: ${e.message}")
        }
    }
    
    /**
     * Obtiene y muestra información detallada de un track específico
     */
    suspend fun getAndDisplayTrackDetails(trackId: String) {
        println("\n🎵 Obteniendo detalles del track ID: $trackId")
        try {
            val track = trackRepository.getTrackById(trackId)
            printDetailedTrackInfo(track)
        } catch (e: SpotifyApiException) {
            println("❌ Error al obtener track: ${e.message}")
        }
    }
    
    /**
     * Obtiene y muestra los top tracks de un artista
     */
    suspend fun getAndDisplayArtistTopTracks(artistId: String) {
        println("\n🎤 Obteniendo top tracks del artista ID: $artistId")
        try {
            val topTracks = trackRepository.getArtistTopTracks(artistId)
            if (topTracks.isNotEmpty()) {
                println("✅ Top tracks:")
                topTracks.forEachIndexed { index, track ->
                    println("  ${index + 1}. ${track.name} - Popularidad: ${track.popularity}/100")
                }
            } else {
                println("❌ No se encontraron top tracks para el artista")
            }
        } catch (e: SpotifyApiException) {
            println("❌ Error al obtener top tracks: ${e.message}")
        }
    }
    
    private fun printDetailedTrackInfo(track: Track) {
        println("\n" + "=".repeat(50))
        println("🎵 DETALLES DEL TRACK")
        println("=".repeat(50))
        println("ID: ${track.id}")
        println("Nombre: ${track.name}")
        println("Artistas: ${track.artists.joinToString(", ") { it.name }}")
        println("Álbum: ${track.album.name}")
        println("Duración: ${formatDuration(track.durationMs)}")
        println("Popularidad: ${track.popularity}/100")
        println("Explícito: ${if (track.explicit) "Sí" else "No"}")
        println("Número de track: ${track.trackNumber}")
        println("Preview URL: ${track.previewUrl ?: "No disponible"}")
        println("=".repeat(50))
    }
}

suspend fun main() {
    // ⚠️ REEMPLAZA CON TUS CREDENCIALES REALES
    val clientId = "43551abad28b4f9290ed67904ee20f5e"
    val clientSecret = "dd2408b1ccae4bdca9fd71735f6649eb"
    
    // PASO 1: Crear el servicio de autenticación (implementación concreta)
    val authService: AuthService = SpotifyAuthService(clientId, clientSecret)
    
    // PASO 2: Crear los repositorios inyectando la dependencia (Inyección de Dependencias)
    val searchRepository: SearchRepository = SpotifySearchRepository(authService)
    val trackRepository: TrackRepository = SpotifyTrackRepository(authService)
    
    // PASO 3: Crear el servicio principal que programa contra interfaces (DIP)
    val spotifyService = SpotifyService(searchRepository, trackRepository)
    
    try {
        println("=".repeat(70))
        println("🎼 DEMO DE CAPA DE ABSTRACCIÓN SOBRE API DE SPOTIFY")
        println("=".repeat(70))
        println("📚 Principios implementados:")
        println("  • Inversión de Dependencias (DIP): Programamos contra interfaces")
        println("  • Inyección de Dependencias: Los repositorios reciben AuthService")
        println("  • Separación de responsabilidades: Cada clase tiene una función específica")
        println("=".repeat(70))
        
        // DEMOSTRACIÓN DE BÚSQUEDAS
        spotifyService.searchAndDisplayTracks("Bohemian Rhapsody", 3)
        spotifyService.searchAndDisplayArtists("Queen", 3)
        spotifyService.searchAndDisplayAlbums("A Night at the Opera", 3)
        
        // DEMOSTRACIÓN DE OPERACIONES ESPECÍFICAS DE TRACKS
        val trackId = "4u7EnebtmKWzUH433cf5Qv" // Bohemian Rhapsody
        spotifyService.getAndDisplayTrackDetails(trackId)
        
        val artistId = "1dfeR4HaWDbWqFHLkxsg1d" // Queen
        spotifyService.getAndDisplayArtistTopTracks(artistId)
        
        println("\n" + "=".repeat(70))
        println("✅ DEMOSTRACIÓN COMPLETADA EXITOSAMENTE")
        println("🏗️ La capa de abstracción oculta la complejidad de la API")
        println("🔌 Los repositorios son intercambiables sin cambiar el código cliente")
        println("🧪 El código es fácil de testear usando mocks de las interfaces")
        println("=".repeat(70))
        
    } catch (e: Exception) {
        println("❌ Error general: ${e.message}")
        e.printStackTrace()
    } finally {
        // Cerrar recursos si es necesario
        if (authService is SpotifyAuthService) {
            authService.close()
        }
        if (searchRepository is SpotifySearchRepository) {
            searchRepository.close()
        }
        if (trackRepository is SpotifyTrackRepository) {
            trackRepository.close()
        }
    }
}

// Función utilitaria reutilizada
private fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes}:${seconds.toString().padStart(2, '0')}"
}