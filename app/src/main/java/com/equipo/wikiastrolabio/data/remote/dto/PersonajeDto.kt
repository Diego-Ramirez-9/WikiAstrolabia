package com.equipo.wikiastrolabio.data.remote.dto

import kotlinx.serialization.Serializable

/** Elemento de GET /personajes */
@Serializable
data class PersonajeResumenDto(
    val slug: String,
    val nombre: String,
    val rol: String,
    val resumen: String? = null,
    val imagenUrl: String? = null,
)

/** GET /personajes/:slug */
@Serializable
data class PersonajeDto(
    val slug: String,
    val nombre: String,
    val titulo: String? = null,
    val rol: String,
    val estado: String = "activo",
    val resumen: String? = null,
    val origen: String? = null,
    val motivacion: String? = null,
    val personalidad: String? = null,
    val arco: String? = null,
    val imagenUrl: String? = null,
    /** Clave = categoría ("apariencia", "expresion"…). Se conserva el orden del JSON. */
    val rasgos: Map<String, List<RasgoDto>> = emptyMap(),
    val dialogos: List<DialogoDto> = emptyList(),
)

@Serializable
data class RasgoDto(
    val nombre: String,
    val descripcion: String,
)
