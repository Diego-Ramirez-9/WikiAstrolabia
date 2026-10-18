package com.equipo.wikiastrolabio.domain.usecase.mundo

import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.Zona
import com.equipo.wikiastrolabio.domain.repository.MundoRepository
import javax.inject.Inject

/** Detalle de una Casa o zona con sus salas. */
class ObtenerZonaUseCase @Inject constructor(
    private val repositorio: MundoRepository,
) {
    suspend operator fun invoke(slug: String, forzarRed: Boolean = false): Resultado<Zona> =
        repositorio.obtenerZona(slug, forzarRed)
}
