package com.equipo.wikiastrolabio.domain.model

/** Enlace a otro elemento del códex (por ejemplo, la zona a la que pertenece una sala). */
data class Referencia(
    val slug: String,
    val nombre: String,
)

/** Diálogo de Efeméride: aparece en el detalle de un personaje y en el de una sala. */
data class Dialogo(
    val slug: String,
    val titulo: String?,
    val tipo: String?,
    val momento: String?,
    val lineas: List<LineaDialogo>,
)

data class LineaDialogo(
    val orden: Int,
    val texto: String,
    /** Acotación de cámara o sonido. */
    val indicacion: String?,
    /** Lugar del recorrido donde se dice (solo en indicaciones). */
    val lugar: String?,
)
