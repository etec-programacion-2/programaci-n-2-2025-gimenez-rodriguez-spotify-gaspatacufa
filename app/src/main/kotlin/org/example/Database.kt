package org.example

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.ResultSet

object Database {
    private val dataSource: HikariDataSource
    
    init {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://localhost:3306/spotify_db"
            username = "root"  
            password = "1234"  
            driverClassName = "com.mysql.cj.jdbc.Driver"
            maximumPoolSize = 10
            
            // Configuración adicional
            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        }
        dataSource = HikariDataSource(config)
        println("✅ Conexión a MySQL establecida")
    }
    
    fun getConnection(): Connection {
        return dataSource.connection
    }
    
    fun close() {
        dataSource.close()
    }
    
    // Buscar artista por nombre
    fun buscarArtistaPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM artistas WHERE nombre LIKE ? LIMIT 1"
        getConnection().use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return rs.getString("spotify_id")
                }
            }
        }
        return "NO SE ENCUENTRAA ARTISTA"
    }
    
    // Buscar canción por nombre
    fun buscarCancionPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM canciones WHERE nombre LIKE ? LIMIT 1"
        getConnection().use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return rs.getString("spotify_id")
                }
            }
        }
        return "NO SE ENCUENTRAA CANCION"
    }
    
    // Buscar álbum por nombre
    fun buscarAlbumPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM albumes WHERE nombre LIKE ? LIMIT 1"
        getConnection().use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return rs.getString("spotify_id")
                }
            }
        }
        return "NO SE ENCUENTRAA ALBUMM"
    }
    
    // Buscar playlist por nombre
    fun buscarPlaylistPorNombre(nombre: String): String? {
        val query = "SELECT spotify_id FROM playlists WHERE nombre LIKE ? LIMIT 1"
        getConnection().use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$nombre%")
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return rs.getString("spotify_id")
                }
            }
        }
        return "NO SE ENCUENTRAA PLAYLIST"
    }
    
    // Guardar artista en BD
    fun guardarArtista(artist: Artist) {
        val query = """
            INSERT INTO artistas (spotify_id, nombre, generos, popularidad, seguidores)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            nombre = VALUES(nombre),
            generos = VALUES(generos),
            popularidad = VALUES(popularidad),
            seguidores = VALUES(seguidores)
        """
        getConnection().use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, artist.id)
                stmt.setString(2, artist.name)
                stmt.setString(3, artist.genres.joinToString(", "))
                stmt.setInt(4, artist.popularity)
                stmt.setLong(5, artist.followers?.total?.toLong() ?: 0)
                stmt.executeUpdate()
            }
        }
    }
}