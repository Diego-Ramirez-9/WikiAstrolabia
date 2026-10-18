package com.equipo.wikiastrolabio.data.remote.fake

import android.content.res.AssetManager
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.FileNotFoundException

/**
 * Simula la API mientras no existe: responde cada petición con un JSON de
 * `src/debug/assets/fake-api/` sin tocar la red. Solo se instala si BuildConfig.USE_FAKE_API.
 *
 * Como actúa a nivel HTTP, todo lo demás (Retrofit, DTO, caché en Room, errores) funciona
 * exactamente igual que con la API real: cambiar a la API real es solo apagar la bandera.
 *
 * Correspondencia de rutas (la misma que usa tools/fake-api/generar_fake_api.py):
 *   GET {base}/personajes/almanaque-xii  ->  fake-api/personajes/almanaque-xii.json
 *   GET {base}/zonas?tipo=casa           ->  fake-api/zonas.tipo-casa.json
 * Si el archivo no existe responde 404 con el formato de error de la API.
 */
class FakeApiInterceptor(
    /** Devuelve el contenido del archivo o null si no existe. */
    private val leerArchivo: (ruta: String) -> ByteArray?,
    urlBase: String,
    /** Retraso artificial para ver los estados de carga como con la red real. */
    private val latenciaMs: Long = 400,
) : Interceptor {

    private val prefijoRuta: List<String> = urlBase.toHttpUrl().pathSegments.filter { it.isNotEmpty() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val peticion = chain.request()
        val archivo = archivoPara(peticion.url)
        if (latenciaMs > 0) Thread.sleep(latenciaMs)

        val cuerpo = leerArchivo(archivo)

        val (codigo, contenido) = if (cuerpo != null) {
            200 to cuerpo
        } else {
            404 to """{"error":{"codigo":"NO_ENCONTRADO","mensaje":"Sin respuesta falsa para '$archivo'."}}"""
                .toByteArray()
        }
        return Response.Builder()
            .request(peticion)
            .protocol(Protocol.HTTP_1_1)
            .code(codigo)
            .message(if (codigo == 200) "OK" else "Not Found")
            .header("Content-Type", TIPO_JSON)
            .body(contenido.toResponseBody(TIPO_JSON.toMediaType()))
            .build()
    }

    private fun archivoPara(url: HttpUrl): String {
        val segmentos = url.pathSegments.filter { it.isNotEmpty() }
        val ruta = (if (segmentos.take(prefijoRuta.size) == prefijoRuta) segmentos.drop(prefijoRuta.size) else segmentos)
            .joinToString("/")
        val consulta = url.queryParameterNames.sorted()
            .joinToString(".") { nombre -> "$nombre-${url.queryParameter(nombre).orEmpty()}" }
        return buildString {
            append(CARPETA).append('/').append(ruta)
            if (consulta.isNotEmpty()) append('.').append(consulta)
            append(".json")
        }
    }

    companion object {
        private const val CARPETA = "fake-api"
        private const val TIPO_JSON = "application/json; charset=utf-8"

        /** Lee las respuestas de src/debug/assets/fake-api. */
        fun desdeAssets(assets: AssetManager, urlBase: String) = FakeApiInterceptor(
            leerArchivo = { ruta ->
                try {
                    assets.open(ruta).use { it.readBytes() }
                } catch (_: FileNotFoundException) {
                    null
                }
            },
            urlBase = urlBase,
        )
    }
}
