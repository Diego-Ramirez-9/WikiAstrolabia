package com.equipo.wikiastrolabio.data.repository

import com.equipo.wikiastrolabio.data.local.cache.CacheRespuestaEntity
import com.equipo.wikiastrolabio.data.local.preferencias.PreferenciasContenido
import com.equipo.wikiastrolabio.di.RedModule
import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import com.equipo.wikiastrolabio.domain.model.OrigenDatos
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.testutil.CacheRespuestaDaoFalso
import com.equipo.wikiastrolabio.testutil.DataStoreEnMemoria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class CacheDeContenidoTest {

    private val dao = CacheRespuestaDaoFalso()
    private val preferencias = PreferenciasContenido(DataStoreEnMemoria())
    private val json = RedModule.provideJson()
    private val cache = CacheDeContenido(dao, preferencias, json, Dispatchers.Unconfined)

    private var descargas = 0

    private fun descargaExitosa(valor: List<String>): suspend () -> List<String> = {
        descargas++
        valor
    }

    private fun descargaFallida(error: Exception): suspend () -> List<String> = {
        descargas++
        throw error
    }

    private fun guardarCopia(valor: List<String>, version: String?) {
        dao.filas["clave"] = CacheRespuestaEntity("clave", json.encodeToString(valor), version, 0)
    }

    @Test
    fun `sin copia descarga y guarda`() = runTest {
        preferencias.guardarVersionContenido("v1")

        val resultado = cache.obtener("clave", forzarRed = false, descargar = descargaExitosa(listOf("a")))

        assertEquals(Resultado.Exito(listOf("a"), OrigenDatos.RED), resultado)
        assertEquals("v1", dao.filas["clave"]?.versionContenido)
    }

    @Test
    fun `copia de la version actual se usa sin red`() = runTest {
        preferencias.guardarVersionContenido("v1")
        guardarCopia(listOf("guardado"), version = "v1")

        val resultado = cache.obtener("clave", forzarRed = false, descargar = descargaExitosa(listOf("nuevo")))

        assertEquals(Resultado.Exito(listOf("guardado"), OrigenDatos.CACHE), resultado)
        assertEquals(0, descargas)
    }

    @Test
    fun `copia de otra version se vuelve a descargar`() = runTest {
        preferencias.guardarVersionContenido("v2")
        guardarCopia(listOf("viejo"), version = "v1")

        val resultado = cache.obtener("clave", forzarRed = false, descargar = descargaExitosa(listOf("nuevo")))

        assertEquals(Resultado.Exito(listOf("nuevo"), OrigenDatos.RED), resultado)
        assertEquals("v2", dao.filas["clave"]?.versionContenido)
    }

    @Test
    fun `forzarRed ignora la copia vigente`() = runTest {
        preferencias.guardarVersionContenido("v1")
        guardarCopia(listOf("guardado"), version = "v1")

        val resultado = cache.obtener("clave", forzarRed = true, descargar = descargaExitosa(listOf("nuevo")))

        assertEquals(Resultado.Exito(listOf("nuevo"), OrigenDatos.RED), resultado)
    }

    @Test
    fun `sin conexion usa la copia aunque sea vieja`() = runTest {
        guardarCopia(listOf("guardado"), version = "v0")

        val resultado = cache.obtener("clave", forzarRed = false, descargar = descargaFallida(IOException()))

        assertEquals(Resultado.Exito(listOf("guardado"), OrigenDatos.CACHE_SIN_CONEXION), resultado)
    }

    @Test
    fun `sin conexion y sin copia devuelve error`() = runTest {
        val resultado = cache.obtener("clave", forzarRed = false, descargar = descargaFallida(IOException()))

        assertEquals(Resultado.Error(ErrorDatos.SinConexion), resultado)
    }

    @Test
    fun `un 404 borra la copia y devuelve NoEncontrado`() = runTest {
        guardarCopia(listOf("guardado"), version = "v0")
        val error404 = HttpException(Response.error<Any>(404, "".toResponseBody()))

        val resultado = cache.obtener("clave", forzarRed = true, descargar = descargaFallida(error404))

        assertEquals(Resultado.Error(ErrorDatos.NoEncontrado), resultado)
        assertNull(dao.filas["clave"])
    }

    @Test
    fun `un 500 usa la copia guardada`() = runTest {
        guardarCopia(listOf("guardado"), version = "v0")
        val error500 = HttpException(Response.error<Any>(500, """{"error":{"codigo":"X"}}""".toResponseBody()))

        val resultado = cache.obtener("clave", forzarRed = false, descargar = descargaFallida(error500))

        assertEquals(Resultado.Exito(listOf("guardado"), OrigenDatos.CACHE_SIN_CONEXION), resultado)
    }

    @Test
    fun `una copia que ya no coincide con el DTO se descarta`() = runTest {
        preferencias.guardarVersionContenido("v1")
        dao.filas["clave"] = CacheRespuestaEntity("clave", """{"formato":"viejo"}""", "v1", 0)

        val resultado = cache.obtener("clave", forzarRed = false, descargar = descargaExitosa(listOf("nuevo")))

        assertTrue(resultado is Resultado.Exito && resultado.origen == OrigenDatos.RED)
        assertFalse(dao.filas["clave"]!!.json.contains("viejo"))
    }
}
