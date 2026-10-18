package com.equipo.wikiastrolabio.domain.repository

import com.equipo.wikiastrolabio.domain.model.EstadoSincronizacion

/**
 * Controla la vigencia de la caché. La API expone la fecha del último cambio del contenido;
 * si no cambió desde la última vez, las pantallas leen de Room sin descargar nada.
 */
interface ContenidoRepository {
    /** GET /contenido/version. Se llama al iniciar la app (mientras se ve el splash). */
    suspend fun sincronizarVersion(): EstadoSincronizacion
}
