package com.equipo.wikiastrolabio.domain.model

import java.time.Instant

/** Reseña del juego escrita por un jugador (sin cuenta, solo con un apodo). */
data class Resena(
    val id: String,
    val apodo: String,
    /** De 1 a 5 estrellas. */
    val calificacion: Int,
    val texto: String,
    /** null si la API mandó una fecha que no se pudo leer. */
    val creadoEn: Instant?,
)

/** Contenido de la pantalla Reseñas. */
data class ResumenResenas(
    /** Reseñas publicadas en total (no solo las de [resenas]). */
    val total: Int,
    /** Calificación promedio con un decimal; null si todavía no hay reseñas. */
    val promedio: Double?,
    /** Las más recientes primero. */
    val resenas: List<Resena>,
)

/** Lo que escribe el jugador en el formulario. */
data class NuevaResena(
    val apodo: String,
    val calificacion: Int,
    val texto: String,
)

/**
 * Las mismas reglas que valida la API (docs/ENDPOINTS.md, ruta 15). La app las usa para
 * habilitar el botón y mostrar los contadores; la API tiene la última palabra (por ejemplo, rechaza los enlaces).
 */
object ReglasResena {
    val APODO = 2..30
    val TEXTO = 10..500
    val CALIFICACION = 1..5

    /** Caracteres reales (un emoji cuenta como uno), sin los espacios de los extremos. */
    fun longitud(texto: String): Int = texto.trim().let { it.codePointCount(0, it.length) }

    fun esValida(apodo: String, calificacion: Int, texto: String): Boolean =
        longitud(apodo) in APODO && calificacion in CALIFICACION && longitud(texto) in TEXTO
}
