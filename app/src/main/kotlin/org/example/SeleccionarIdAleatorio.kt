package org.example

class SeleccionarIdAleatorio(private val archivo: String) { // clase que selecciona un id aleatorio de un archivo
    fun run(): String? { // funcion que devuelve un id aleatorio o null
        val ids = LeerIdsDeArchivo(archivo).run() // lee los ids del archivo
        return if (ids.isNotEmpty()) ids.random() else null // si hay ids devuelve uno aleatorio, sino devuelve null
    }
}
