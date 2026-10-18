package com.equipo.wikiastrolabio.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Envoltorio de todas las respuestas exitosas: `{ "data": ... }`.
 *
 * Reglas para los DTO de este paquete (ver docs/ENDPOINTS.md de la API):
 * - Las propiedades se llaman igual que las claves JSON (camelCase), sin @SerialName.
 * - Todo lo que la API puede omitir o mandar en null lleva valor por defecto, para que
 *   un cambio pequeño en el backend no tumbe la pantalla.
 * - Los DTO no salen de la capa de datos: los mappers los convierten a modelos de dominio.
 */
@Serializable
data class RespuestaApi<T>(val data: T)

/** Cuerpo de las respuestas con error: `{ "error": { "codigo": "...", "mensaje": "..." } }`. */
@Serializable
data class RespuestaErrorApi(val error: DetalleErrorApi)

@Serializable
data class DetalleErrorApi(
    val codigo: String = "",
    val mensaje: String? = null,
)
