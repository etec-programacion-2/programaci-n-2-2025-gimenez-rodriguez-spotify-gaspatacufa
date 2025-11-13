ANUAL DE USO SPOTIFY CONSULTOR

DESCRIPCION GENERAL:

Spotify Consulter es una aplicación interactiva desarrollada en Kotlin que permite a los usuarios explorar información detallada de la API de Spotify de manera simple y directa. El proyecto funciona como una herramienta educativa que demuestra la integración con servicios web externos, manejo de bases de datos y desarrollo de interfaces tanto por consola como web.

Requisitos:
-Java 21
-Gradle: 9.0.0


Credenciales: 

En el archivo Autenticacion.kt, verificar que esten la credenciales de Spotify: 

private val clientId = "43551abad28b4f9290ed67904ee20f5e" //LINEA 5
private val clientSecret = "dd2408b1ccae4bdca9fd71735f6649eb" //LINEA 6

EJECUCION:

Paso 1: Clona repositorio

git clone git@github.com:etec-programacion-2/programaci-n-2-2025-gimenez-rodriguez-spotify-gaspatacufa.git

Paso 2: Ejecucion

Modo Consola, Ejecutar:

gradle run

Explicion Modo Consola, Algo asi se ve al ejecutar:

════════════════════════════════════════════════════════════════════
╔════════════════════════════════════════════════════════════════════╗
║                         MENÚ PRINCIPAL                             ║
╚════════════════════════════════════════════════════════════════════╝
  1. 🎤 Consultar Artista
  2. 🎵 Consultar Canción/Track
  3. 💿 Consultar Álbum
  4. 📀 Consultar Playlist
  5. 🎲 Consultar Todo (Aleatorio)
  6. 🚪 Salir
════════════════════════════════════════════════════════════════════

Opciones disponibles:

Opcion 1 - consultar artista:

muestra lista de ids de artistas disponibles
puedes elegir:

1-seleccionar de la lista (numero)
2-ingresar id manualmente
3-elegir uno aleatorio

Opcion 2 - consultar cancion (mismo metedo que en artista):

1-seleccionar de la lista (numero)
2-ingresar id manualmente
3-elegir uno aleatorio

Opcion 3 - consultar album (mismo metedo que en artista):

1-seleccionar de la lista (numero)
2-ingresar id manualmente
3-elegir uno aleatorio

Opcion 4 - consultar playlist (mismo metedo que en artista):

1-seleccionar de la lista (numero)
2-ingresar id manualmente
3-elegir uno aleatorio

Opcion 5 - consultar todo aleatorio:

Ejecuta el modo aleatorio automaticamente.  

Modo web, Ejecutar: 

./gradlew run --args="--web"

Se vera este mensaje: 

"🌐 Servidor web iniciado en: http://localhost:8080"
Realizar ctrl + click en url, para abrir la web.

Para detener el servidor: 

ctrl + c
s

Comando Rapidos:

Modo consola:

gradle run

Modo web: 

./gradlew run --args="--web"

Solucion de algun problema: 

Puerto 8080 ocupado: 

Cambia el puerto en WebServer.kt (//LINEA 11): 
class WebServer(private val spotifyClient: SpotifyApiClient, private val port: Int = 9090) 

A: 

class WebServer(private val spotifyClient: SpotifyApiClient, private val port: Int = 8080)

