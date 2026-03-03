package org.example

import java.io.File // importa clase file
import java.util.Scanner // importa scanner para leer entrada del usuario

data class Ejecutar( // clase que ejecuta el menu interactivo
    val spotifyClient: SpotifyApiClient, // cliente de spotify para hacer consultas
    val archivoArtistas: String, // ruta del archivo de artistas
    val archivoAlbumes: String, // ruta del archivo de albumes
    val archivoPistas: String, // ruta del archivo de pistas
    val archivoPlaylists: String // ruta del archivo de playlists
) {
    private val scanner = Scanner(System.`in`) // scanner para leer del teclado
    
    suspend fun ejecutar() { // funcion principal que ejecuta el programa
        try {
            println("\n" + "=".repeat(70)) // imprime linea de separacion
            println("BIENVENIDO A LA CONSOLA DE SPOTIFY") // imprime titulo de bienvenida
            println("=".repeat(70)) // imprime linea de separacion

            var continuar = true // variable que controla el loop principal
            while (continuar) { // loop mientras continuar sea true
                mostrarMenu() // muestra el menu de opciones
                
                print("\nSelecciona una opción: ") // pide al usuario que elija
                val input = scanner.nextLine().trim() // lee la entrada y quita espacios
                val opcion = input.toIntOrNull() // intenta convertir a numero, devuelve null si falla

                when (opcion) { // evalua la opcion elegida
                    1 -> consultarArtista() // si es 1, consulta artista
                    2 -> consultarTrack() // si es 2, consulta cancion
                    3 -> consultarAlbum() // si es 3, consulta album
                    4 -> consultarPlaylist() // si es 4, consulta playlist
                    5 -> consultarTodoAleatorio() // si es 5, consulta todo aleatoriamente
                    6 -> { // si es 6, sale del programa
                        println("\n¡Hasta luego!") // imprime mensaje de despedida
                        continuar = false // cambia continuar a false para salir del loop
                    }
                    else -> println("\n❌ Opción inválida. Por favor, selecciona un número del 1 al 6.") // si no es ninguna opcion valida
                }

                if (continuar && opcion != null) { // si el programa continua y la opcion es valida
                    println("\n" + "-".repeat(70)) // imprime linea de separacion
                    print("Presiona ENTER para continuar...") // pide presionar enter
                    scanner.nextLine() // espera a que el usuario presione enter
                }
            }

        } catch (e: Exception) { // si hay algun error
            println("❌ Error general: ${e.message}") // imprime el error
            e.printStackTrace() // imprime el detalle del error
        } finally { // se ejecuta siempre al final
            scanner.close() // cierra el scanner
            spotifyClient.close() // cierra el cliente de spotify
        }
    }

    private fun mostrarMenu() { // funcion que muestra el menu principal
        println("\n╔════════════════════════════════════════════════════════════════════╗") // imprime borde superior
        println("║                         MENÚ PRINCIPAL                             ║") // imprime titulo
        println("╚════════════════════════════════════════════════════════════════════╝") // imprime borde inferior
        println("  1. 🎤 Consultar Artista") // opcion 1
        println("  2. 🎵 Consultar Canción/Track") // opcion 2
        println("  3. 💿 Consultar Álbum") // opcion 3
        println("  4. 📀 Consultar Playlist") // opcion 4
        println("  5. 🎲 Consultar Todo (Aleatorio)") // opcion 5
        println("  6. 🚪 Salir") // opcion 6
    }

    private fun cargarIds(archivo: String): List<String> { // funcion que carga ids desde un archivo
        return try {
            File(archivo).readLines() // lee todas las lineas del archivo
                .map { it.trim() } // quita espacios en blanco de cada linea
                .filter { it.isNotBlank() } // filtra lineas que no esten vacias
        } catch (e: Exception) { // si hay error
            println("❌ Error al cargar archivo $archivo: ${e.message}") // imprime el error
            emptyList() // devuelve lista vacia
        }
    }

    private fun mostrarListaIds(ids: List<String>, tipo: String) { // funcion que muestra una lista numerada de ids
        println("\n📋 IDs de $tipo disponibles:") // imprime titulo
        ids.forEachIndexed { index, id -> // itera sobre cada id con su indice
            println("  ${index + 1}. $id") // imprime el numero (indice+1) y el id
        }
    }

    private suspend fun consultarArtista() { // funcion que consulta un artista
        println("\n" + "=".repeat(70)) // imprime linea de separacion
        println("CONSULTAR ARTISTA") // imprime titulo
        println("=".repeat(70)) // imprime linea de separacion
        
        val artistasIds = cargarIds(archivoArtistas) // carga los ids de artistas
        
        if (artistasIds.isEmpty()) { // si no hay artistas
            println("❌ No hay artistas disponibles") // imprime mensaje de error
            return // sale de la funcion
        }

        mostrarListaIds(artistasIds, "Artistas") // muestra la lista de ids
        
        println("\nElige una opción:") // pide elegir opcion
        println("  1. Seleccionar de la lista") // opcion 1
        println("  2. Ingresar ID manualmente") // opcion 2
        println("  3. Aleatorio") // opcion 3
        print("Opción: ") // pide la opcion
        
        val opcion = scanner.nextLine().trim().toIntOrNull() // lee y convierte a numero

        val artistaId = when (opcion) { // evalua la opcion
            1 -> { // si elige de la lista
                print("Ingresa el número del artista (1-${artistasIds.size}): ") // pide el numero
                val num = scanner.nextLine().trim().toIntOrNull() // lee y convierte a numero
                if (num != null && num in 1..artistasIds.size) { // si el numero es valido
                    artistasIds[num - 1] // obtiene el id (restando 1 porque la lista empieza en 0)
                } else { // si el numero no es valido
                    println("❌ Número inválido") // imprime error
                    null // devuelve null
                }
            }
            2 -> { // si elige ingresar manualmente
                print("Ingresa el ID del artista: ") // pide el id
                scanner.nextLine().trim() // lee el id
            }
            3 -> artistasIds.random() // si elige aleatorio, selecciona uno al azar
            else -> { // si la opcion no es valida
                println("❌ Opción inválida") // imprime error
                null // devuelve null
            }
        }

        if (artistaId != null) { // si se obtuvo un id
            val artist = spotifyClient.getArtist(artistaId) // consulta el artista en spotify
            if (artist != null) { // si se obtuvo el artista
                ArtistPrinter(artist).print() // imprime la informacion del artista
            }
        } else { // si no se obtuvo id
            println("❌ No se pudo obtener el ID del artista") // imprime error
        }
    }

    private suspend fun consultarTrack() { // funcion que consulta una cancion
        println("\n" + "=".repeat(70)) // imprime linea de separacion
        println("CONSULTAR CANCIÓN") // imprime titulo
        println("=".repeat(70)) // imprime linea de separacion
        
        val pistasIds = cargarIds(archivoPistas) // carga los ids de pistas
        
        if (pistasIds.isEmpty()) { // si no hay pistas
            println("❌ No hay pistas disponibles") // imprime mensaje de error
            return // sale de la funcion
        }

        mostrarListaIds(pistasIds, "Pistas") // muestra la lista de ids
        
        println("\nElige una opción:") // pide elegir opcion
        println("  1. Seleccionar de la lista") // opcion 1
        println("  2. Ingresar ID manualmente") // opcion 2
        println("  3. Aleatorio") // opcion 3
        print("Opción: ") // pide la opcion
        
        val opcion = scanner.nextLine().trim().toIntOrNull() // lee y convierte a numero

        val trackId = when (opcion) { // evalua la opcion
            1 -> { // si elige de la lista
                print("Ingresa el número de la pista (1-${pistasIds.size}): ") // pide el numero
                val num = scanner.nextLine().trim().toIntOrNull() // lee y convierte a numero
                if (num != null && num in 1..pistasIds.size) { // si el numero es valido
                    pistasIds[num - 1] // obtiene el id
                } else { // si el numero no es valido
                    println("❌ Número inválido") // imprime error
                    null // devuelve null
                }
            }
            2 -> { // si elige ingresar manualmente
                print("Ingresa el ID de la canción: ") // pide el id
                scanner.nextLine().trim() // lee el id
            }
            3 -> pistasIds.random() // si elige aleatorio, selecciona uno al azar
            else -> { // si la opcion no es valida
                println("❌ Opción inválida") // imprime error
                null // devuelve null
            }
        }

        if (trackId != null) { // si se obtuvo un id
            val track = spotifyClient.getTrack(trackId) // consulta la cancion en spotify
            if (track != null) { // si se obtuvo la cancion
                TrackPrinter(track).print() // imprime la informacion de la cancion
            }
        } else { // si no se obtuvo id
            println("❌ No se pudo obtener el ID de la canción") // imprime error
        }
    }

    private suspend fun consultarAlbum() { // funcion que consulta un album (similar a las anteriores)
        println("\n" + "=".repeat(70))
        println("CONSULTAR ÁLBUM")
        println("=".repeat(70))
        
        val albumesIds = cargarIds(archivoAlbumes)
        
        if (albumesIds.isEmpty()) {
            println("❌ No hay álbumes disponibles")
            return
        }

        mostrarListaIds(albumesIds, "Álbumes")
        
        println("\nElige una opción:")
        println("  1. Seleccionar de la lista")
        println("  2. Ingresar ID manualmente")
        println("  3. Aleatorio")
        print("Opción: ")
        
        val opcion = scanner.nextLine().trim().toIntOrNull()

        val albumId = when (opcion) {
            1 -> {
                print("Ingresa el número del álbum (1-${albumesIds.size}): ")
                val num = scanner.nextLine().trim().toIntOrNull()
                if (num != null && num in 1..albumesIds.size) {
                    albumesIds[num - 1]
                } else {
                    println("❌ Número inválido")
                    null
                }
            }
            2 -> {
                print("Ingresa el ID del álbum: ")
                scanner.nextLine().trim()
            }
            3 -> albumesIds.random()
            else -> {
                println("❌ Opción inválida")
                null
            }
        }

        if (albumId != null) {
            val album = spotifyClient.getAlbum(albumId)
            if (album != null) {
                AlbumPrinter(album).print()
            }
        } else {
            println("❌ No se pudo obtener el ID del álbum")
        }
    }

    private suspend fun consultarPlaylist() { // funcion que consulta una playlist (similar a las anteriores)
        println("\n" + "=".repeat(70))
        println("CONSULTAR PLAYLIST")
        println("=".repeat(70))
        
        val playlistsIds = cargarIds(archivoPlaylists)
        
        if (playlistsIds.isEmpty()) {
            println("❌ No hay playlists disponibles")
            return
        }

        mostrarListaIds(playlistsIds, "Playlists")
        
        println("\nElige una opción:")
        println("  1. Seleccionar de la lista")
        println("  2. Ingresar ID manualmente")
        println("  3. Aleatorio")
        print("Opción: ")
        
        val opcion = scanner.nextLine().trim().toIntOrNull()

        val playlistId = when (opcion) {
            1 -> {
                print("Ingresa el número de la playlist (1-${playlistsIds.size}): ")
                val num = scanner.nextLine().trim().toIntOrNull()
                if (num != null && num in 1..playlistsIds.size) {
                    playlistsIds[num - 1]
                } else {
                    println("❌ Número inválido")
                    null
                }
            }
            2 -> {
                print("Ingresa el ID de la playlist: ")
                scanner.nextLine().trim()
            }
            3 -> playlistsIds.random()
            else -> {
                println("❌ Opción inválida")
                null
            }
        }

        if (playlistId != null) {
            val playlist = spotifyClient.getPlaylist(playlistId)
            if (playlist != null) {
                PlaylistPrinter(playlist).print()
            }
        } else {
            println("❌ No se pudo obtener el ID de la playlist")
        }
    }

    private suspend fun consultarTodoAleatorio() { // funcion que consulta todo aleatoriamente
        println("\n" + "=".repeat(70))
        println("CONSULTANDO TODO ALEATORIAMENTE")
        println("=".repeat(70))

        val artistasIds = cargarIds(archivoArtistas) // carga ids de artistas
        val pistasIds = cargarIds(archivoPistas) // carga ids de pistas
        val albumesIds = cargarIds(archivoAlbumes) // carga ids de albumes
        val playlistsIds = cargarIds(archivoPlaylists) // carga ids de playlists

        if (artistasIds.isNotEmpty()) { // si hay artistas
            val artistaId = artistasIds.random() // selecciona uno aleatorio
            println("\n🔍 Consultando artista aleatorio...")
            val artist = spotifyClient.getArtist(artistaId) // consulta el artista
            if (artist != null) {
                ArtistPrinter(artist).print() // imprime la info
            }
        }
        
        if (pistasIds.isNotEmpty()) { // si hay pistas
            val pistaId = pistasIds.random() // selecciona una aleatoria
            println("\n🔍 Consultando pista aleatoria...")
            val track = spotifyClient.getTrack(pistaId) // consulta la pista
            if (track != null) {
                TrackPrinter(track).print() // imprime la info
            }
        }
        
        if (albumesIds.isNotEmpty()) { // si hay albumes
            val albumId = albumesIds.random() // selecciona uno aleatorio
            println("\n🔍 Consultando álbum aleatorio...")
            val album = spotifyClient.getAlbum(albumId) // consulta el album
            if (album != null) {
                AlbumPrinter(album).print() // imprime la info
            }
        }
        
        if (playlistsIds.isNotEmpty()) { // si hay playlists
            val playlistId = playlistsIds.random() // selecciona una aleatoria
            println("\n🔍 Consultando playlist aleatoria...")
            val playlist = spotifyClient.getPlaylist(playlistId) // consulta la playlist
            if (playlist != null) {
                PlaylistPrinter(playlist).print() // imprime la info
            }
        }

        println("\n✅ Todas las consultas completadas") // mensaje final
    }
}