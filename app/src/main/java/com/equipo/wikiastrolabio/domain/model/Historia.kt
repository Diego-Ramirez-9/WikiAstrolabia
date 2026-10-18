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
