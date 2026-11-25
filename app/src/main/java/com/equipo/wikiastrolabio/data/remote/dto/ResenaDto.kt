package com.equipo.wikiastrolabio.data.remote.dto

import kotlinx.serialization.Serializable

/** GET /resenas: total, promedio (null si no hay reseñas) y las 50 más recientes. */
@Serializable
data class ResumenResenasDto(
    val total: Int = 0,
    val promedio: Double? = null,
    val resenas: List<ResenaDto> = emptyList(),
)

@Serializable
data class ResenaDto(
    val id: String,
    val apodo: String = "",
    val calificacion: Int = 0,
    val texto: String = "",
    /** Fecha ISO 8601 en UTC: "2026-11-25T17:45:31.120Z". */
    val creadoEn: String? = null,
)

/** Cuerpo de POST /resenas. */
@Serializable
data class NuevaResenaDto(
    val apodo: String,
    val calificacion: Int,
    val texto: String,
)
