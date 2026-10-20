package com.equipo.wikiastrolabio.domain.usecase.historia

import com.equipo.wikiastrolabio.domain.model.LecturaHistoria
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.map
import com.equipo.wikiastrolabio.domain.repository.HistoriaRepository
import javax.inject.Inject

/**
 * Entrada de la Historia lista para leer: el texto, su número de capítulo y las entradas
 * anterior y siguiente (en el orden del índice) para leer de corrido.
 * Si el índice no se puede consultar, la entrada se muestra igual, sin vecinas.
 */
class ObtenerEntradaHistoriaUseCase @Inject constructor(
    private val repositorio: HistoriaRepository,
) {
    suspend operator fun invoke(slug: String, forzarRed: Boolean = false): Resultado<LecturaHistoria> {
        val indice = (repositorio.obtenerHistoria(forzarRed) as? Resultado.Exito)?.datos.orEmpty()
        val todas = indice.flatMap { it.entradas }
        val posicion = todas.indexOfFirst { it.slug == slug }
        val capitulo = indice.indexOfFirst { seccion -> seccion.entradas.any { it.slug == slug } }

        return repositorio.obtenerEntrada(slug, forzarRed).map { entrada ->
            LecturaHistoria(
                entrada = entrada,
                numeroCapitulo = if (capitulo >= 0) capitulo + 1 else null,
                anterior = todas.getOrNull(posicion - 1).takeIf { posicion > 0 },
                siguiente = todas.getOrNull(posicion + 1).takeIf { posicion >= 0 },
            )
        }
    }
}
