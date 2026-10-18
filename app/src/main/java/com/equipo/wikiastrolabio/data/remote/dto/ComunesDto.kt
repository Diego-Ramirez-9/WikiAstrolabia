package com.equipo.wikiastrolabio.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReferenciaDto(
    val slug: String,
    val nombre: String,
)

/** En /salas/:slug solo trae slug y líneas; en /personajes/:slug trae también título, tipo y momento. */
@Serializable
data class DialogoDto(
    val slug: String,
    val titulo: String? = null,
    val tipo: String? = null,
    val momento: String? = null,
    val lineas: List<LineaDialogoDto> = emptyList(),
)

@Serializable
data class LineaDialogoDto(
    val orden: Int,
    val texto: String,
    val indicacion: String? = null,
    val lugar: String? = null,
)
