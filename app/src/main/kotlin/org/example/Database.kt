package org.example

import java.sql.Connection
import java.sql.DriverManager

object Database {
    private var connection: Connection? = null
    
    // Ubicación de tu archivo SQLite
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
    
    fun buscarArtistaPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM artistas WHERE nombre LIKE ? LIMIT 1"
        return try {
            getConnection().prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    rs.getString("spotify_id")
                } else null
            }
        } catch (e: Exception) {
            println("❌ Error al buscar artista: ${e.message}")
            null
        }
    }
    
    fun buscarCancionPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM canciones WHERE nombre LIKE ? LIMIT 1"
        return try {
            getConnection().prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    rs.getString("spotify_id")
                } else null
            }
        } catch (e: Exception) {
            println("❌ Error al buscar canción: ${e.message}")
            null
        }
    }
    
    fun buscarAlbumPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM albumes WHERE nombre LIKE ? LIMIT 1"
        return try {
            getConnection().prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    rs.getString("spotify_id")
                } else null
            }
        } catch (e: Exception) {
            println("❌ Error al buscar álbum: ${e.message}")
            null
        }
    }

    fun buscarPlaylistPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM playlists WHERE nombre LIKE ? LIMIT 1"
        return try {
            getConnection().prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    rs.getString("spotify_id")
                } else null
            }
        } catch (e: Exception) {
            println("❌ Error al buscar playlist: ${e.message}")
            null
        }
    }
}
    

