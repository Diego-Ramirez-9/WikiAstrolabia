package com.equipo.wikiastrolabio.domain.model

enum class RolPersonaje { PROTAGONISTA, GUIA, SECUNDARIO, COLECTIVO, DESCONOCIDO }

enum class EstadoPersonaje { ACTIVO, DESACTIVADO, DESAPARECIDO, DESCONOCIDO }

/** Tarjeta de la lista de Personajes. */
data class PersonajeResumen(
    val slug: String,
    val nombre: String,
    val rol: RolPersonaje,
    val resumen: String?,
    val imagenUrl: String?,
)

/** Ficha completa ("Ver más"). */
data class Personaje(
    val slug: String,
    val nombre: String,
    val titulo: String?,
    val rol: RolPersonaje,
    val estado: EstadoPersonaje,
    val resumen: String?,
    val origen: String?,
    val motivacion: String?,
    val personalidad: String?,
    val arco: String?,
    val imagenUrl: String?,
    /** Rasgos agrupados por categoría, en el orden en que llegan de la API. */
    val rasgos: List<GrupoRasgos>,
    /** Solo los tiene quien habla (Efeméride). */
    val dialogos: List<Dialogo>,
)

/**
 * La categoría se deja como texto ("apariencia", "herramienta"…) para que una categoría
 * nueva en la base de datos aparezca sin cambiar la app.
 */
data class GrupoRasgos(
    val categoria: String,
    val rasgos: List<Rasgo>,
)

data class Rasgo(
    val nombre: String,
    val descripcion: String,
)
