Data class facilita la forma en hacer las clases porque generan las funciones comunes que usamos
automaticamente como: tostring, equals, hashcode, copy y component.

Lo que no tienen las data class que tienen las class es que son mas flexibles, pero, hay que
escribir automaticamente las funciones que usamos. 

Tarea 4, modulado de la api:

La capa que esta aislada de lo demas se encuentra en el archivo SpotifyApiClient.kt, ahi se hacen la peticiones de todo lo que pedimos que es: artista, album, cancion y playlist. 

Maneja lo headers, que son como reglas que le damos a la peticion para que sepa quienes somos, en que idioma, etc. Tambien parsea las respuesta, significa que convierte los datos que recibe de la spotify en objetos(artist, track, album, playlist, etc). 

Maneja lo errores que son el:
200: todo funciona bien
401: no hay credenciales validas, es por un toke expirado, no se dio el token.
403: estan las credenciales validad, pero no hay permisos para hacer la peticion.
429: muchas peticiones en corto periodo, hay que esperar un tiempo para volver hacer peticiones, que lo dice un header http. 

Lo que implementamos nuevo es una "mini base de datos" son 4 .txt uno de artista, otro de cancion, otro de playlist y otro de albumn. En cada uno hay unaa cantidad de Id para los diferentes tipos de peticiones. 
Lo que hacemos con las librerias java.io.File y java.util.* que leen los .txt y elije una aleatorio, para despues guardarlo en una varible y que ese sea el id pra las peticion. Esto se hace en Utils.kt en la funcion, leerIdsDeArchivo y seleccionarIdAleatorio.

