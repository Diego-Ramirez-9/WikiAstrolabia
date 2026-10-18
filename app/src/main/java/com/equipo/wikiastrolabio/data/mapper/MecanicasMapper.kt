package com.equipo.wikiastrolabio.data.mapper

import com.equipo.wikiastrolabio.data.remote.dto.HabilidadDto
import com.equipo.wikiastrolabio.data.remote.dto.MecanicasDto
import com.equipo.wikiastrolabio.data.remote.dto.ReglaDto
import com.equipo.wikiastrolabio.domain.model.Habilidad
import com.equipo.wikiastrolabio.domain.model.Mecanicas
import com.equipo.wikiastrolabio.domain.model.Regla
import com.equipo.wikiastrolabio.domain.model.TipoHabilidad
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun tipoHabilidadDesdeApi(valor: String?): TipoHabilidad = when (valor) {
    "mecanica" -> TipoHabilidad.MECANICA
    "narrativa" -> TipoHabilidad.NARRATIVA
    else -> TipoHabilidad.DESCONOCIDO
}

fun MecanicasDto.aDominio() = Mecanicas(
    habilidades = habilidades.sortedBy { it.numero }.map { it.aDominio() },
    reglas = reglas.map { it.aDominio() },
)

fun HabilidadDto.aDominio() = Habilidad(
    slug = slug,
    numero = numero,
    nombreCorto = nombreCorto,
    nombre = nombre,
    etiqueta = etiqueta,
    tipo = tipoHabilidadDesdeApi(tipo),
    descripcion = descripcion,
    origenMundo = origenMundo,
    senalJugador = senalJugador,
    parametros = parametros.mapValues { (_, valor) -> valor.comoTexto() },
    iconoUrl = iconoUrl,
)

fun ReglaDto.aDominio() = Regla(slug = slug, nombre = nombre, descripcion = descripcion)

/** `5` -> "5", `"variable"` -> "variable", `["latón","piedra"]` -> "latón, piedra". */
private fun JsonElement.comoTexto(): String = when (this) {
    is JsonNull -> ""
    is JsonPrimitive -> content
    is JsonArray -> joinToString(", ") { it.comoTexto() }
    is JsonObject -> toString()
}
