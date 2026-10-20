package com.equipo.wikiastrolabio.data.mapper

import com.equipo.wikiastrolabio.data.remote.dto.DialogoDto
import com.equipo.wikiastrolabio.data.remote.dto.LineaDialogoDto
import com.equipo.wikiastrolabio.data.remote.dto.ReferenciaDto
import com.equipo.wikiastrolabio.domain.model.Dialogo
import com.equipo.wikiastrolabio.domain.model.LineaDialogo
import com.equipo.wikiastrolabio.domain.model.Referencia
import com.equipo.wikiastrolabio.domain.model.TipoDialogo

fun ReferenciaDto.aDominio() = Referencia(slug = slug, nombre = nombre)

fun tipoDialogoDesdeApi(valor: String?): TipoDialogo = when (valor) {
    "escena" -> TipoDialogo.ESCENA
    "indicacion" -> TipoDialogo.INDICACION
    "cita" -> TipoDialogo.CITA
    else -> TipoDialogo.DESCONOCIDO
}

fun DialogoDto.aDominio() = Dialogo(
    slug = slug,
    titulo = titulo,
    tipo = tipoDialogoDesdeApi(tipo),
    momento = momento,
    personaje = personaje?.aDominio(),
    zona = zona?.aDominio(),
    sala = sala?.aDominio(),
    lineas = lineas.sortedBy { it.orden }.map { it.aDominio() },
)

fun LineaDialogoDto.aDominio() = LineaDialogo(
    orden = orden,
    texto = texto,
    indicacion = indicacion,
    lugar = lugar,
)
