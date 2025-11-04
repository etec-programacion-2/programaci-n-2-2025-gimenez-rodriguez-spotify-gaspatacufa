package org.example

import java.io.File

class LeerIdsDeArchivo(private val archivo: String) { // clase que lee ids desde un archivo
    fun run(): List<String> { // funcion que devuelve lista de ids
        val file = File(archivo) // crea objeto file con la ruta
        if (!file.exists()) { // verifica si el archivo existe
            println("El archivo $archivo no se encuentra en la ruta especificada.") // imprime mensaje de error
            return emptyList() // devuelve lista vacia
        }
        return file.readLines() // lee todas las lineas del archivo y las devuelve como lista
    }
}
