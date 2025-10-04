package org.example

import java.io.File

fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes}:${seconds.toString().padStart(2, '0')}"
}

fun leerIdsDeArchivo(archivo: String): List<String> {
    val file = File(archivo)
    if (!file.exists()) {
        println("El archivo $archivo no se encuentra en la ruta especificada.")
        return emptyList()  
    }
    return file.readLines()  
}

fun seleccionarIdAleatorio(archivo: String): String? {
    val ids = leerIdsDeArchivo(archivo)
    return if (ids.isNotEmpty()) {
        ids.random()  
    } else {
        null  
    }
}