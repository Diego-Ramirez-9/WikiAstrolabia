package com.equipo.wikiastrolabio.domain.usecase.contenido

import com.equipo.wikiastrolabio.domain.model.EstadoSincronizacion
import com.equipo.wikiastrolabio.domain.repository.ContenidoRepository
import javax.inject.Inject

/**
 * Se ejecuta al abrir la app: consulta si el contenido de la API cambió desde la última vez.
 * Si cambió, las pantallas descargarán de nuevo; si no, leerán la caché sin usar la red.
 */
class SincronizarContenidoUseCase @Inject constructor(
    private val repositorio: ContenidoRepository,
) {
    suspend operator fun invoke(): EstadoSincronizacion = repositorio.sincronizarVersion()
}
