package com.equipo.wikiastrolabio.domain.model

enum class TipoHabilidad {
    /** Habilidad jugable (Salto, Ancla, Gancho, Escalar): la que muestra el Figma. */
    MECANICA,

    /** Acción narrativa (Observar, Descansar…). */
    NARRATIVA,

    DESCONOCIDO,
}

/** Todo el contenido de la pantalla Mecánicas. */
data class Mecanicas(
    val habilidades: List<Habilidad>,
    val reglas: List<Regla>,
) {
    val jugables: List<Habilidad> get() = habilidades.filter { it.tipo == TipoHabilidad.MECANICA }
    val narrativas: List<Habilidad> get() = habilidades.filter { it.tipo == TipoHabilidad.NARRATIVA }
}

data class Habilidad(
    val slug: String,
    val numero: Int,
    /** Título de la tarjeta: "Ancla", "Escalar". */
    val nombreCorto: String,
    val nombre: String,
    /** Texto del chip: "5 s", "2 usos". */
    val etiqueta: String?,
    val tipo: TipoHabilidad,
    val descripcion: String,
    val origenMundo: String?,
    val senalJugador: String?,
    /** Valores de ajuste ya convertidos a texto (ej. "duracion_s" -> "5"). Pueden cambiar. */
    val parametros: Map<String, String>,
    val iconoUrl: String?,
)

/** Regla general del juego (Recarga, Superficies, Muerte…). */
data class Regla(
    val slug: String,
    val nombre: String,
    val descripcion: String,
)
