package com.equipo.wikiastrolabio.data.mapper

import com.equipo.wikiastrolabio.data.remote.dto.ArchivoDto
import com.equipo.wikiastrolabio.data.remote.dto.ArchivoResumenDto
import com.equipo.wikiastrolabio.data.remote.dto.CategoriaArchivoDto
import com.equipo.wikiastrolabio.domain.model.Archivo
import com.equipo.wikiastrolabio.domain.model.ArchivoResumen
import com.equipo.wikiastrolabio.domain.model.CategoriaArchivo

fun CategoriaArchivoDto.aDominio() = CategoriaArchivo(
    slug = slug,
    nombre = nombre,
    descripcion = descripcion,
    iconoUrl = iconoUrl,
    elementos = elementos.map { it.aDominio() },
)

fun ArchivoResumenDto.aDominio() = ArchivoResumen(
    slug = slug,
    titulo = titulo,
    ubicacion = ubicacion,
    imagenUrl = imagenUrl,
)

fun ArchivoDto.aDominio() = Archivo(
    slug = slug,
    titulo = titulo,
    categoria = categoria,
    contenido = contenido,
    autor = autor,
    ubicacion = ubicacion,
    imagenUrl = imagenUrl,
    zona = zona?.aDominio(),
    sala = sala?.aDominio(),
)
