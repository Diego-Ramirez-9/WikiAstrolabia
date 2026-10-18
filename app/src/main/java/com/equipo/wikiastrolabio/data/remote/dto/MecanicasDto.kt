package com.equipo.wikiastrolabio.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/** GET /mecanicas */
@Serializable
data class MecanicasDto(
    val habilidades: List<HabilidadDto> = emptyList(),
    val reglas: List<ReglaDto> = emptyList(),
)

@Serializable
data class HabilidadDto(
    val slug: String,
    val numero: Int = 0,
    val nombreCorto: String,
    val nombre: String,
    val etiqueta: String? = null,
    val tipo: String,
    val descripcion: String,
    val origenMundo: String? = null,
    val senalJugador: String? = null,
    /** Objeto libre (jsonb): números, textos o listas según la habilidad. */
    val parametros: JsonObject = JsonObject(emptyMap()),
    val iconoUrl: String? = null,
)

@Serializable
data class ReglaDto(
    val slug: String,
    val nombre: String,
    val descripcion: String,
)
