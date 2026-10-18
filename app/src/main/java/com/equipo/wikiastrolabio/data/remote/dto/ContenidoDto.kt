package com.equipo.wikiastrolabio.data.remote.dto

import kotlinx.serialization.Serializable

/** GET /salud */
@Serializable
data class SaludDto(
    val estado: String,
    val baseDeDatos: String? = null,
)

/** GET /contenido/version: fecha ISO-8601 del último cambio en cualquier tabla. */
@Serializable
data class VersionContenidoDto(val actualizadoEn: String)
