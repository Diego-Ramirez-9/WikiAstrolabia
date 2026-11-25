package com.equipo.wikiastrolabio.domain.usecase.resenas

import com.equipo.wikiastrolabio.domain.model.NuevaResena
import com.equipo.wikiastrolabio.domain.model.Resena
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.ResumenResenas
import com.equipo.wikiastrolabio.domain.repository.ResenasRepository
import javax.inject.Inject

/** Promedio, total y reseñas recientes de la pantalla Reseñas. */
class ObtenerResenasUseCase @Inject constructor(
    private val repositorio: ResenasRepository,
) {
    suspend operator fun invoke(): Resultado<ResumenResenas> = repositorio.obtenerResenas()
}

class PublicarResenaUseCase @Inject constructor(
    private val repositorio: ResenasRepository,
) {
    suspend operator fun invoke(resena: NuevaResena): Resultado<Resena> = repositorio.publicar(resena)

    /** Apodo con el que se rellena el formulario. */
    suspend fun apodoGuardado(): String? = repositorio.apodoGuardado()
}
