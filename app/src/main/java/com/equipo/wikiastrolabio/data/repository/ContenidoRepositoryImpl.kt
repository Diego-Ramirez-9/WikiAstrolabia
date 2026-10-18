package com.equipo.wikiastrolabio.data.repository

import com.equipo.wikiastrolabio.data.local.preferencias.PreferenciasContenido
import com.equipo.wikiastrolabio.data.remote.AstrolabioApi
import com.equipo.wikiastrolabio.domain.model.EstadoSincronizacion
import com.equipo.wikiastrolabio.domain.repository.ContenidoRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ContenidoRepositoryImpl @Inject constructor(
    private val api: AstrolabioApi,
    private val preferencias: PreferenciasContenido,
) : ContenidoRepository {

    /** Nunca lanza excepciones: si algo falla, la app sigue con lo que tiene guardado. */
    override suspend fun sincronizarVersion(): EstadoSincronizacion = try {
        val remota = api.versionContenido().data.actualizadoEn
        if (remota == preferencias.versionContenido()) {
            EstadoSincronizacion.SIN_CAMBIOS
        } else {
            // Las copias guardadas con la versión anterior quedan vencidas y CacheDeContenido
            // las renueva conforme se abre cada pantalla.
            preferencias.guardarVersionContenido(remota)
            EstadoSincronizacion.HAY_CONTENIDO_NUEVO
        }
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        EstadoSincronizacion.SIN_CONEXION
    }
}
