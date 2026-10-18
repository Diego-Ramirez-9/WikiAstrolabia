package com.equipo.wikiastrolabio.domain.model

/** Acordeón de la pantalla Archivos (Poemas, Notas de XI…) con sus elementos. */
data class CategoriaArchivo(
    val slug: String,
    val nombre: String,
    val descripcion: String?,
    val iconoUrl: String?,
    val elementos: List<ArchivoResumen>,
)

data class ArchivoResumen(
    val slug: String,
    val titulo: String,
    /** Texto libre de dónde se encuentra en el juego. */
    val ubicacion: String?,
    val imagenUrl: String?,
)

/** Detalle de un poema o nota. */
data class Archivo(
    val slug: String,
    val titulo: String,
    /** Slug de la categoría ("poemas", "notas-de-xi"). */
    val categoria: String,
    val contenido: String,
    val autor: String?,
    val ubicacion: String?,
    val imagenUrl: String?,
    val zona: Referencia?,
    val sala: Referencia?,
)
