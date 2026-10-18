package com.equipo.wikiastrolabio.data.remote

import com.equipo.wikiastrolabio.data.remote.fake.FakeApiInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Test

class FakeApiInterceptorTest {

    private val pedidos = mutableListOf<String>()
    private val archivos = mapOf(
        "fake-api/personajes/almanaque-xii.json" to """{"data":{}}""",
        "fake-api/zonas.tipo-casa.json" to """{"data":[]}""",
    )

    private val cliente = OkHttpClient.Builder()
        .addInterceptor(
            FakeApiInterceptor(
                leerArchivo = { ruta -> pedidos += ruta; archivos[ruta]?.toByteArray() },
                urlBase = "https://api.ejemplo.com/api/v1/",
                latenciaMs = 0,
            )
        )
        .build()

    private fun get(url: String) = cliente.newCall(Request.Builder().url(url).build()).execute()

    @Test
    fun `quita el prefijo de la url base y agrega json`() {
        get("https://api.ejemplo.com/api/v1/personajes/almanaque-xii").use { respuesta ->
            assertEquals(200, respuesta.code)
            assertEquals("""{"data":{}}""", respuesta.body.string())
        }
        assertEquals("fake-api/personajes/almanaque-xii.json", pedidos.last())
    }

    @Test
    fun `los parametros de consulta forman parte del nombre del archivo`() {
        get("https://api.ejemplo.com/api/v1/zonas?tipo=casa").use { assertEquals(200, it.code) }
        assertEquals("fake-api/zonas.tipo-casa.json", pedidos.last())
    }

    @Test
    fun `sin archivo responde 404 con el formato de error de la API`() {
        get("https://api.ejemplo.com/api/v1/personajes/no-existe").use { respuesta ->
            assertEquals(404, respuesta.code)
            assertEquals(true, respuesta.body.string().contains("NO_ENCONTRADO"))
        }
    }
}
