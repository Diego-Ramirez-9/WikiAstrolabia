package com.equipo.wikiastrolabio.data.mapper

import com.equipo.wikiastrolabio.data.remote.dto.ResenaDto
import com.equipo.wikiastrolabio.data.remote.dto.ResumenResenasDto
import com.equipo.wikiastrolabio.domain.model.Resena
import com.equipo.wikiastrolabio.domain.model.ResumenResenas
import java.time.Instant

fun ResumenResenasDto.aDominio() = ResumenResenas(
    total = total,
    promedio = promedio,
    resenas = resenas.map { it.aDominio() },
)

fun ResenaDto.aDominio() = Resena(
    id = id,
    apodo = apodo,
    calificacion = calificacion.coerceIn(1, 5),
    texto = texto,
    creadoEn = creadoEn?.let { runCatching { Instant.parse(it) }.getOrNull() },
)
