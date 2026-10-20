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
    /** false = sala final sin plataformas (la Cámara Central). */
    val esNivel: Boolean,
    val esTutorial: Boolean,
    val etiquetas: List<String>,
    val resumen: String?,
    val tonoEmocional: String?,
    val ideaCentral: String?,
    val exterior: String?,
    val ambientacion: String?,
    val narrativa: String?,
    /** Qué ocurre al restaurar la Casa. */
    val restauracion: String?,
    val coleccionables: String?,
    /** Descripción de la paleta de color del nivel. */
    val paleta: String?,
    val iluminacion: String?,
    val sensacionVisual: String?,
    /** Nombre del color característico ("violeta", "turquesa"); la UI lo usa como acento. */
    val tinte: String?,
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
    /** Posición dentro del recorrido de su zona (1, 2, 3…). */
    val orden: Int,
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

/** Observatorio mayor: zona segura con telescopio y notas del Almanaque XI. */
data class Observatorio(
    val slug: String,
    val nombre: String,
    val ubicacion: String?,
    val resumen: String?,
    val exterior: String?,
    val interior: String?,
    val luz: String?,
    /** Ambiente sonoro. */
    val audio: String?,
    val descubrimientos: String?,
    /** Qué contiene (ej. "Primeras notas de XI: el descubrimiento."). */
    val contenidoBreve: String?,
    val imagenUrl: String?,
)

/**
 * Una sala junto con su lugar en el recorrido de la zona, para navegar a la anterior y a la
 * siguiente sin regresar a la lista.
 */
data class RecorridoSala(
    val sala: Sala,
    val anterior: SalaResumen?,
    val siguiente: SalaResumen?,
    /** Número total de salas de la zona (0 si no se pudo consultar). */
    val total: Int,
    /** Tipo y tinte de la zona, para que la sala use el mismo color de acento. */
    val tipoZona: TipoZona = TipoZona.DESCONOCIDO,
    val tinteZona: String? = null,
)

/** Archivo (poema o nota) que se encuentra en una sala. */
data class ArchivoEnSala(
    val slug: String,
    val titulo: String,
    val categoria: String,
)
