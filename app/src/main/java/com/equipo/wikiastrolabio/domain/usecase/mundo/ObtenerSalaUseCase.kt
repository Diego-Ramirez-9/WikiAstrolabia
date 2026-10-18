package com.equipo.wikiastrolabio.domain.usecase.mundo

import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.Sala
import com.equipo.wikiastrolabio.domain.repository.MundoRepository
import javax.inject.Inject

/** Detalle de una sala: observatorio, archivos y diálogos. */
class ObtenerSalaUseCase @Inject constructor(
    private val repositorio: MundoRepository,
) {
    suspend operator fun invoke(slug: String, forzarRed: Boolean = false): Resultado<Sala> =
        repositorio.obtenerSala(slug, forzarRed)
}
