package com.equipo.wikiastrolabio.data.remote.dto

import kotlinx.serialization.Serializable

/** GET /mapa */
@Serializable
data class MapaDto(
    val titulo: String,
    val descripcion: String? = null,
    val imagenUrl: String? = null,
    val puntos: List<PuntoMapaDto> = emptyList(),
)

@Serializable
data class PuntoMapaDto(
    val slug: String,
    val nombre: String,
    val subtitulo: String? = null,
    val tipo: String,
    val resumen: String? = null,
    val x: Float,
    val y: Float,
)

/** Elemento de GET /zonas y GET /zonas?tipo=casa */
@Serializable
data class ZonaResumenDto(
    val slug: String,
    val nombre: String,
    val subtitulo: String? = null,
    val tipo: String,
    val etiquetas: List<String> = emptyList(),
    val imagenUrl: String? = null,
)

/** GET /zonas/:slug */
@Serializable
data class ZonaDto(
    val slug: String,
    val nombre: String,
    val subtitulo: String? = null,
    val tipo: String = "",
    val constelacion: ConstelacionDto? = null,
    val esNivel: Boolean = true,
    val esTutorial: Boolean = false,
    val etiquetas: List<String> = emptyList(),
    val resumen: String? = null,
    val tonoEmocional: String? = null,
    val ideaCentral: String? = null,
    val exterior: String? = null,
    val ambientacion: String? = null,
    val narrativa: String? = null,
    val restauracion: String? = null,
    val coleccionables: String? = null,
    val paleta: String? = null,
    val iluminacion: String? = null,
    val sensacionVisual: String? = null,
    /** Nombre del color de la Casa ("violeta", "turquesa"…). */
    val tinteFondo: String? = null,
    val imagenUrl: String? = null,
    val salas: List<SalaResumenDto> = emptyList(),
    val mecanicas: List<MecanicaZonaDto> = emptyList(),
)

@Serializable
data class ConstelacionDto(
    val nombre: String,
    val sobrenombre: String? = null,
)

@Serializable
data class SalaResumenDto(
    val slug: String,
    val nombre: String,
    val orden: Int = 0,
    val esZonaSegura: Boolean = false,
    val esObservatorio: Boolean = false,
)

@Serializable
data class MecanicaZonaDto(
    val slug: String,
    val nombre: String,
    val descripcion: String,
)

/** GET /salas/:slug */
@Serializable
data class SalaDto(
    val slug: String,
    val nombre: String,
    val orden: Int = 0,
    val descripcion: String? = null,
    val aprendizaje: String? = null,
    val esZonaSegura: Boolean = false,
    val imagenUrl: String? = null,
    val zona: ReferenciaDto? = null,
    val observatorio: ObservatorioDto? = null,
    val archivos: List<ArchivoEnSalaDto> = emptyList(),
    val dialogos: List<DialogoDto> = emptyList(),
)

@Serializable
data class ObservatorioDto(
    val slug: String = "",
    val nombre: String,
    val ubicacion: String? = null,
    val resumen: String? = null,
    val exterior: String? = null,
    val interior: String? = null,
    val luz: String? = null,
    val audio: String? = null,
    val descubrimientos: String? = null,
    val contenidoBreve: String? = null,
    val imagenUrl: String? = null,
)

@Serializable
data class ArchivoEnSalaDto(
    val slug: String,
    val titulo: String,
    val categoria: String,
)
