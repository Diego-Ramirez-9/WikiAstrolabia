package com.equipo.wikiastrolabio.domain.usecase.mundo

import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen
import com.equipo.wikiastrolabio.domain.repository.MundoRepository
import javax.inject.Inject

/** Casas jugables para las pestañas de la pantalla Zonas. */
class ObtenerCasasUseCase @Inject constructor(
    private val repositorio: MundoRepository,
) {
    suspend operator fun invoke(forzarRed: Boolean = false): Resultado<List<ZonaResumen>> =
        repositorio.obtenerZonas(TipoZona.CASA, forzarRed)
}
