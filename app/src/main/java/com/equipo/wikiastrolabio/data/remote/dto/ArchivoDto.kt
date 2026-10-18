package com.equipo.wikiastrolabio.data.remote.dto

import kotlinx.serialization.Serializable

/** Elemento de GET /archivos: una categoría (acordeón) con sus elementos. */
@Serializable
data class CategoriaArchivoDto(
    val slug: String,
    val nombre: String,
    val descripcion: String? = null,
    val iconoUrl: String? = null,
    val elementos: List<ArchivoResumenDto> = emptyList(),
)

@Serializable
data class ArchivoResumenDto(
    val slug: String,
    val titulo: String,
    val ubicacion: String? = null,
    val imagenUrl: String? = null,
)

/** GET /archivos/:slug */
@Serializable
data class ArchivoDto(
    val slug: String,
    val titulo: String,
    val categoria: String,
    val contenido: String,
    val autor: String? = null,
    val ubicacion: String? = null,
    val imagenUrl: String? = null,
    val zona: ReferenciaDto? = null,
    val sala: ReferenciaDto? = null,
)
