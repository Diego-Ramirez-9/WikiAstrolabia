package com.equipo.wikiastrolabio.data.mapper

import com.equipo.wikiastrolabio.data.remote.dto.EntradaHistoriaDto
import com.equipo.wikiastrolabio.data.remote.dto.SeccionHistoriaDto
import com.equipo.wikiastrolabio.domain.model.EntradaHistoria
import com.equipo.wikiastrolabio.domain.model.SeccionHistoria
import com.equipo.wikiastrolabio.domain.model.SeccionResumen

fun SeccionHistoriaDto.aDominio() = SeccionHistoria(
    slug = slug,
    titulo = titulo,
    entradas = entradas.map { it.aDominio() },
)

fun EntradaHistoriaDto.aDominio() = EntradaHistoria(
    slug = slug,
    titulo = titulo,
    contenido = contenido,
    imagenUrl = imagenUrl,
    seccion = seccion?.let { SeccionResumen(slug = it.slug, titulo = it.titulo) },
)
