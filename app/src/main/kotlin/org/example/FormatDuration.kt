package org.example

class FormatDuration(private val durationMs: Long) { // clase que formatea duracion de milisegundos a minutos:segundos
    fun run(): String { // funcion que hace el formateo
        val totalSeconds = durationMs / 1000 // convierte milisegundos a segundos
        val minutes = totalSeconds / 60 // calcula minutos dividiendo segundos por 60
        val seconds = totalSeconds % 60 // calcula segundos restantes usando modulo 60
        return "${minutes}:${seconds.toString().padStart(2, '0')}" // devuelve formato MM:SS, agregando 0 a la izquierda si es necesario
    }
}
