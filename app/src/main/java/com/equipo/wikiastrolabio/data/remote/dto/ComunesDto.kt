package com.equipo.wikiastrolabio.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReferenciaDto(
    val slug: String,
    val nombre: String,
)

/** Diálogo de /personajes/:slug y /salas/:slug: quién habla, dónde ocurre y sus líneas. */
@Serializable
data class DialogoDto(
    val slug: String,
    val titulo: String? = null,
    /** "escena", "indicacion" o "cita". */
    val tipo: String? = null,
    val momento: String? = null,
    val personaje: ReferenciaDto? = null,
    val zona: ReferenciaDto? = null,
    val sala: ReferenciaDto? = null,
    val lineas: List<LineaDialogoDto> = emptyList(),
)

@Serializable
data class LineaDialogoDto(
    val orden: Int,
    val texto: String,
    val indicacion: String? = null,
    val lugar: String? = null,
)
