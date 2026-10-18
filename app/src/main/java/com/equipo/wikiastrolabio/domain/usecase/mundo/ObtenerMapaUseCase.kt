package com.equipo.wikiastrolabio.domain.usecase.mundo

import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.repository.MundoRepository
import javax.inject.Inject

/** Mapa del Gran Reloj con sus marcadores (pantalla Mundo). */
class ObtenerMapaUseCase @Inject constructor(
    private val repositorio: MundoRepository,
) {
    suspend operator fun invoke(forzarRed: Boolean = false): Resultado<Mapa> =
        repositorio.obtenerMapa(forzarRed)
}
