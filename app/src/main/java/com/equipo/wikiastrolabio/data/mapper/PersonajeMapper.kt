package com.equipo.wikiastrolabio.data.mapper

import com.equipo.wikiastrolabio.data.remote.dto.PersonajeDto
import com.equipo.wikiastrolabio.data.remote.dto.PersonajeResumenDto
import com.equipo.wikiastrolabio.domain.model.EstadoPersonaje
import com.equipo.wikiastrolabio.domain.model.GrupoRasgos
import com.equipo.wikiastrolabio.domain.model.Personaje
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.domain.model.Rasgo
import com.equipo.wikiastrolabio.domain.model.RolPersonaje

fun rolDesdeApi(valor: String?): RolPersonaje = when (valor) {
    "protagonista" -> RolPersonaje.PROTAGONISTA
    "guia" -> RolPersonaje.GUIA
    "secundario" -> RolPersonaje.SECUNDARIO
    "colectivo" -> RolPersonaje.COLECTIVO
    else -> RolPersonaje.DESCONOCIDO
}

fun estadoDesdeApi(valor: String?): EstadoPersonaje = when (valor) {
    "activo" -> EstadoPersonaje.ACTIVO
    "desactivado" -> EstadoPersonaje.DESACTIVADO
    "desaparecido" -> EstadoPersonaje.DESAPARECIDO
    else -> EstadoPersonaje.DESCONOCIDO
}

fun PersonajeResumenDto.aDominio() = PersonajeResumen(
    slug = slug,
    nombre = nombre,
    rol = rolDesdeApi(rol),
    resumen = resumen,
    imagenUrl = imagenUrl,
)

fun PersonajeDto.aDominio() = Personaje(
    slug = slug,
    nombre = nombre,
    titulo = titulo,
    rol = rolDesdeApi(rol),
    estado = estadoDesdeApi(estado),
    resumen = resumen,
    origen = origen,
    motivacion = motivacion,
    personalidad = personalidad,
    arco = arco,
    imagenUrl = imagenUrl,
    rasgos = rasgos
        .filterValues { it.isNotEmpty() }
        .map { (categoria, lista) ->
            GrupoRasgos(
                categoria = categoria,
                rasgos = lista.map { Rasgo(nombre = it.nombre, descripcion = it.descripcion) },
            )
        },
    dialogos = dialogos.map { it.aDominio() },
)
