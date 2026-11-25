package com.equipo.wikiastrolabio.domain.repository

import com.equipo.wikiastrolabio.domain.model.NuevaResena
import com.equipo.wikiastrolabio.domain.model.Resena
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.ResumenResenas

/*
 * Reseñas del juego. A diferencia del contenido, siempre se piden a la API (cambian a menudo);
 * la última lista descargada solo se usa si no hay conexión.
 */
interface ResenasRepository {
    suspend fun obtenerResenas(): Resultado<ResumenResenas>

    /** Publica la reseña y recuerda el apodo para la próxima vez. */
    suspend fun publicar(resena: NuevaResena): Resultado<Resena>

    /** Último apodo usado en este dispositivo, o null. */
    suspend fun apodoGuardado(): String?
}
