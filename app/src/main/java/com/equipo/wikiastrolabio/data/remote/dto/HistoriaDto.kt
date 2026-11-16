package com.equipo.wikiastrolabio.data.remote.dto

import kotlinx.serialization.Serializable

/** Elemento de GET /historia: un capítulo con su ilustración y sus entradas. */
@Serializable
data class SeccionHistoriaDto(
    val slug: String,
    val titulo: String,
    /** Ilustración del capítulo (API 1.1.0); null mientras no tenga. */
    val imagenUrl: String? = null,
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
