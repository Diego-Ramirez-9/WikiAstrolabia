package com.equipo.wikiastrolabio.domain.model

enum class TipoZona {
    /** El Eje central (las Entrañas, el tutorial). */
    EJE,

    /** Una Casa jugable (Géminis, Acuario). */
    CASA,

    /** La Cámara Central, el desenlace. */
    CIERRE,

    /** Un tipo nuevo que la app todavía no conoce. */
    DESCONOCIDO,
}

/** Pantalla Mundo: el mapa del Gran Reloj con sus marcadores. */
data class Mapa(
    val titulo: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val puntos: List<PuntoMapa>,
)

/**
 * Marcador del mapa. [x] e [y] van de 0 a 1 respecto a la imagen
 * (0,0 = esquina superior izquierda): se multiplican por su ancho y alto en pantalla.
 */
data class PuntoMapa(
    val slug: String,
    val nombre: String,
    val subtitulo: String?,
    val tipo: TipoZona,
    val resumen: String?,
    val x: Float,
    val y: Float,
)

/** Versión corta de una zona, para las pestañas de la pantalla Zonas. */
data class ZonaResumen(
    val slug: String,
    val nombre: String,
    val subtitulo: String?,
    val tipo: TipoZona,
    val etiquetas: List<String>,
    val imagenUrl: String?,
)

/** Detalle de una Casa o zona con sus salas en orden. */
data class Zona(
    val slug: String,
    val nombre: String,
    val subtitulo: String?,
    val tipo: TipoZona,
    val constelacion: Constelacion?,
    val etiquetas: List<String>,
    val resumen: String?,
    val exterior: String?,
    val ambientacion: String?,
    val narrativa: String?,
    val restauracion: String?,
    val imagenUrl: String?,
    val salas: List<SalaResumen>,
    val mecanicas: List<MecanicaZona>,
)

data class Constelacion(
    val nombre: String,
    val sobrenombre: String?,
)

data class SalaResumen(
    val slug: String,
    val nombre: String,
    val orden: Int,
    val esZonaSegura: Boolean,
    val esObservatorio: Boolean,
)

/** Mecánica propia de una Casa (ej. "Anclaje reflejado" en Géminis). */
data class MecanicaZona(
    val slug: String,
    val nombre: String,
    val descripcion: String,
)

/** Detalle de una sala del recorrido. */
data class Sala(
    val slug: String,
    val nombre: String,
    val descripcion: String?,
    /** Lo que se aprende en la sala (solo en el tutorial). */
    val aprendizaje: String?,
    val esZonaSegura: Boolean,
    val imagenUrl: String?,
    val zona: Referencia?,
    val observatorio: Observatorio?,
    val archivos: List<ArchivoEnSala>,
    val dialogos: List<Dialogo>,
)

data class Observatorio(
    val nombre: String,
    val exterior: String?,
    val interior: String?,
    val luz: String?,
    val descubrimientos: String?,
)

/** Archivo (poema o nota) que se encuentra en una sala. */
data class ArchivoEnSala(
    val slug: String,
    val titulo: String,
    val categoria: String,
)
