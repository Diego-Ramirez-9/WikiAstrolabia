package com.equipo.wikiastrolabio.domain.usecase.mundo

import com.equipo.wikiastrolabio.domain.model.RecorridoSala
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.domain.repository.MundoRepository
import javax.inject.Inject

/**
 * Detalle de una sala con su lugar en el recorrido de la zona (anterior, siguiente y total),
 * para avanzar sala por sala. Si la zona no se puede consultar, la sala se muestra igual.
 */
class ObtenerSalaUseCase @Inject constructor(
    private val repositorio: MundoRepository,
) {
    suspend operator fun invoke(slug: String, forzarRed: Boolean = false): Resultado<RecorridoSala> {
        val sala = when (val resultado = repositorio.obtenerSala(slug, forzarRed)) {
            is Resultado.Error -> return resultado
            is Resultado.Exito -> resultado
        }
        val zona = sala.datos.zona
            ?.let { repositorio.obtenerZona(it.slug, forzarRed) as? Resultado.Exito }
            ?.datos
        val salasDeLaZona = zona?.salas.orEmpty()
        val posicion = salasDeLaZona.indexOfFirst { it.slug == slug }

        return Resultado.Exito(
            datos = RecorridoSala(
                sala = sala.datos,
                anterior = salasDeLaZona.getOrNull(posicion - 1).takeIf { posicion > 0 },
                siguiente = salasDeLaZona.getOrNull(posicion + 1).takeIf { posicion >= 0 },
                total = salasDeLaZona.size,
                tipoZona = zona?.tipo ?: TipoZona.DESCONOCIDO,
                tinteZona = zona?.tinte,
            ),
            origen = sala.origen,
        )
    }
}
