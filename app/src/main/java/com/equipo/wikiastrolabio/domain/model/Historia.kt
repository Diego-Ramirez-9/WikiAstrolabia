package com.equipo.wikiastrolabio.domain.model

/** Sección de la pantalla Historia (ej. "Contexto y cosmología") con sus entradas en orden. */
data class SeccionHistoria(
    val slug: String,
    val titulo: String,
    val entradas: List<EntradaHistoria>,
)

data class EntradaHistoria(
    val slug: String,
    val titulo: String,
    val contenido: String,
    val imagenUrl: String?,
    /** Solo viene en el detalle (GET /historia/:slug). */
    val seccion: SeccionResumen? = null,
)

data class SeccionResumen(
    val slug: String,
    val titulo: String,
)

/** Una entrada lista para leer, con su capítulo y las entradas vecinas para seguir leyendo. */
data class LecturaHistoria(
    val entrada: EntradaHistoria,
    /** Posición del capítulo (1, 2, 3…) o null si no se pudo consultar el índice. */
    val numeroCapitulo: Int?,
    val anterior: EntradaHistoria?,
    val siguiente: EntradaHistoria?,
)
