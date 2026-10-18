package com.equipo.wikiastrolabio.data.remote.dto

import kotlinx.serialization.Serializable

/** Elemento de GET /historia. */
@Serializable
data class SeccionHistoriaDto(
    val slug: String,
    val titulo: String,
    val entradas: List<EntradaHistoriaDto> = emptyList(),
)

/** Entrada dentro de GET /historia y respuesta de GET /historia/:slug (que agrega [seccion]). */
@Serializable
data class EntradaHistoriaDto(
    val slug: String,
    val titulo: String,
    val contenido: String,
    val imagenUrl: String? = null,
    val seccion: SeccionResumenDto? = null,
)

@Serializable
data class SeccionResumenDto(
    val slug: String,
    val titulo: String,
)
