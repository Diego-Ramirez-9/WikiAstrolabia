package com.equipo.wikiastrolabio.domain.usecase.mundo

import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen
import com.equipo.wikiastrolabio.domain.repository.MundoRepository
import javax.inject.Inject

/** Zonas del Gran Reloj (todas, o solo las de un [tipo]) para las pestañas de la pantalla Zonas. */
class ObtenerZonasUseCase @Inject constructor(
    private val repositorio: MundoRepository,
) {
    suspend operator fun invoke(tipo: TipoZona? = null, forzarRed: Boolean = false): Resultado<List<ZonaResumen>> =
        repositorio.obtenerZonas(tipo, forzarRed)
}
