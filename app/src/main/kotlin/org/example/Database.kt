package org.example

import java.sql.Connection
import java.sql.DriverManager

// maneja la conexion y consultas a la base de datos sqlite
object Database {
    private var connection: Connection? = null
    private const val DB_PATH = "database/spotify_db.db"
    
    init {
        conectar()
    }
    
    private fun conectar() {
        try {
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection("jdbc:sqlite:$DB_PATH")
            println("Conexión a SQLite establecida: $DB_PATH")
        } catch (e: Exception) {
            println("❌ Error al conectar a SQLite: ${e.message}")
            e.printStackTrace()
        }
    }
    
    fun getConnection(): Connection {
        if (connection == null || connection?.isClosed == true) {
            conectar()
        }
        return connection!!
    }
    
    fun close() {
        connection?.close()
        println("🔒 Conexión a SQLite cerrada")
    }
    
    // busca un artista en la db y devuelve su id de spotify
    fun buscarArtistaPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM artistas WHERE nombre LIKE ? LIMIT 1"
        return try {
            getConnection().prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) rs.getString("spotify_id") else null
            }
        } catch (e: Exception) {
            println("❌ Error al buscar artista: ${e.message}")
            null
        }
    }
    
    // busca una cancion en la db y devuelve su id de spotify
    fun buscarCancionPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM canciones WHERE nombre LIKE ? LIMIT 1"
        return try {
            getConnection().prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) rs.getString("spotify_id") else null
            }
        } catch (e: Exception) {
            println("❌ Error al buscar canción: ${e.message}")
            null
        }
    }
    
    // busca un album en la db y devuelve su id de spotify
    fun buscarAlbumPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM albumes WHERE nombre LIKE ? LIMIT 1"
        return try {
            getConnection().prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) rs.getString("spotify_id") else null
            }
        } catch (e: Exception) {
            println("❌ Error al buscar álbum: ${e.message}")
            null
        }
    }

    // busca una playlist en la db y devuelve su id de spotify
    fun buscarPlaylistPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM playlists WHERE nombre LIKE ? LIMIT 1"
        return try {
            getConnection().prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) rs.getString("spotify_id") else null
            }
        } catch (e: Exception) {
            println("❌ Error al buscar playlist: ${e.message}")
            null
        }
    }
}