package com.equipo.wikiastrolabio.domain.model

/** Enlace a otro elemento del códex (por ejemplo, la zona a la que pertenece una sala). */
data class Referencia(
    val slug: String,
    val nombre: String,
)

enum class TipoDialogo {
    /** Escena completa con varias líneas (inicio o cierre de una zona). */
    ESCENA,

    /** Indicaciones que da Efeméride durante el recorrido. */
    INDICACION,

    /** Frase suelta. */
    CITA,

    DESCONOCIDO,
}

/** Diálogo de Efeméride: aparece en el detalle de un personaje y en el de una sala. */
data class Dialogo(
    val slug: String,
    val titulo: String?,
    val tipo: TipoDialogo,
    /** Cuándo ocurre en el juego. */
    val momento: String?,
    /** Quién habla. */
    val personaje: Referencia?,
    val zona: Referencia?,
    val sala: Referencia?,
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
