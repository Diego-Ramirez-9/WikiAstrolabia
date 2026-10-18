package com.equipo.wikiastrolabio.data.repository

import com.equipo.wikiastrolabio.data.local.cache.CacheRespuestaDao
import com.equipo.wikiastrolabio.data.local.cache.CacheRespuestaEntity
import com.equipo.wikiastrolabio.data.local.preferencias.PreferenciasContenido
import com.equipo.wikiastrolabio.data.remote.aErrorDatos
import com.equipo.wikiastrolabio.di.DispatcherIo
import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import com.equipo.wikiastrolabio.domain.model.OrigenDatos
import com.equipo.wikiastrolabio.domain.model.Resultado
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

/**
 * Estrategia offline-first que comparten todos los repositorios:
 *
 * 1. Si hay copia en Room y su versión coincide con la de /contenido/version → se usa (sin red).
 * 2. Si no, se descarga de la API y se guarda.
 * 3. Si la descarga falla y hay copia (aunque sea vieja) → se usa y se marca CACHE_SIN_CONEXION.
 * 4. Si falla y no hay copia → Resultado.Error.
 *
 * Un 404 no usa la copia: significa que el contenido ya no existe en la API.
 */
@Singleton
class CacheDeContenido @Inject constructor(
    private val dao: CacheRespuestaDao,
    private val preferencias: PreferenciasContenido,
    private val json: Json,
    @param:DispatcherIo private val dispatcherIo: CoroutineDispatcher,
) {

    /** Versión con el serializador deducido del tipo: `cache.obtener<MapaDto>("mapa", forzarRed) { api.mapa().data }`. */
    suspend inline fun <reified T> obtener(
        clave: String,
        forzarRed: Boolean,
        noinline descargar: suspend () -> T,
    ): Resultado<T> = obtener(clave, serializer<T>(), forzarRed, descargar)

    suspend fun <T> obtener(
        clave: String,
        serializador: KSerializer<T>,
        forzarRed: Boolean,
        descargar: suspend () -> T,
    ): Resultado<T> = withContext(dispatcherIo) {
        val versionActual = preferencias.versionContenido()
        val guardado = leer(clave, serializador)

        if (!forzarRed && guardado != null && versionActual != null && guardado.version == versionActual) {
            return@withContext Resultado.Exito(guardado.valor, OrigenDatos.CACHE)
        }

        try {
            val descargado = descargar()
            dao.guardar(
                CacheRespuestaEntity(
                    clave = clave,
                    json = json.encodeToString(serializador, descargado),
                    versionContenido = versionActual,
                    guardadoEn = System.currentTimeMillis(),
                )
            )
            Resultado.Exito(descargado, OrigenDatos.RED)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val error = e.aErrorDatos(json)
            when {
                error is ErrorDatos.NoEncontrado -> {
                    dao.eliminar(clave)
                    Resultado.Error(error)
                }
                guardado != null -> Resultado.Exito(guardado.valor, OrigenDatos.CACHE_SIN_CONEXION)
                else -> Resultado.Error(error)
            }
        }
    }

    private suspend fun <T> leer(clave: String, serializador: KSerializer<T>): Guardado<T>? {
        val fila = dao.obtener(clave) ?: return null
        return try {
            Guardado(json.decodeFromString(serializador, fila.json), fila.versionContenido)
        } catch (_: SerializationException) {
            // El DTO cambió desde que se guardó: la copia ya no sirve.
            dao.eliminar(clave)
            null
        }
    }

    private class Guardado<T>(val valor: T, val version: String?)
}
