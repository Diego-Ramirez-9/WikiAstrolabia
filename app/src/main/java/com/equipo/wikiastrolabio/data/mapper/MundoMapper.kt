package com.equipo.wikiastrolabio.data.mapper

import com.equipo.wikiastrolabio.data.remote.dto.ArchivoEnSalaDto
import com.equipo.wikiastrolabio.data.remote.dto.MapaDto
import com.equipo.wikiastrolabio.data.remote.dto.MecanicaZonaDto
import com.equipo.wikiastrolabio.data.remote.dto.ObservatorioDto
import com.equipo.wikiastrolabio.data.remote.dto.PuntoMapaDto
import com.equipo.wikiastrolabio.data.remote.dto.SalaDto
import com.equipo.wikiastrolabio.data.remote.dto.SalaResumenDto
import com.equipo.wikiastrolabio.data.remote.dto.ZonaDto
import com.equipo.wikiastrolabio.data.remote.dto.ZonaResumenDto
import com.equipo.wikiastrolabio.domain.model.ArchivoEnSala
import com.equipo.wikiastrolabio.domain.model.Constelacion
import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.MecanicaZona
import com.equipo.wikiastrolabio.domain.model.Observatorio
import com.equipo.wikiastrolabio.domain.model.PuntoMapa
import com.equipo.wikiastrolabio.domain.model.Sala
import com.equipo.wikiastrolabio.domain.model.SalaResumen
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.domain.model.Zona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen

/** Valor que usa la API en el campo "tipo" y en el filtro ?tipo= */
val TipoZona.valorApi: String?
    get() = when (this) {
        TipoZona.EJE -> "eje"
        TipoZona.CASA -> "casa"
        TipoZona.CIERRE -> "cierre"
        TipoZona.DESCONOCIDO -> null
    }

fun tipoZonaDesdeApi(valor: String?): TipoZona =
    TipoZona.entries.firstOrNull { it.valorApi == valor } ?: TipoZona.DESCONOCIDO

fun MapaDto.aDominio() = Mapa(
    titulo = titulo,
    descripcion = descripcion,
    imagenUrl = imagenUrl,
    puntos = puntos.map { it.aDominio() },
)

fun PuntoMapaDto.aDominio() = PuntoMapa(
    slug = slug,
    nombre = nombre,
    subtitulo = subtitulo,
    tipo = tipoZonaDesdeApi(tipo),
    resumen = resumen,
    // Se acotan a 0..1 por si la base de datos trae un valor fuera de rango.
    x = x.coerceIn(0f, 1f),
    y = y.coerceIn(0f, 1f),
)

fun ZonaResumenDto.aDominio() = ZonaResumen(
    slug = slug,
    nombre = nombre,
    subtitulo = subtitulo,
    tipo = tipoZonaDesdeApi(tipo),
    etiquetas = etiquetas,
    imagenUrl = imagenUrl,
)

fun ZonaDto.aDominio() = Zona(
    slug = slug,
    nombre = nombre,
    subtitulo = subtitulo,
    tipo = tipoZonaDesdeApi(tipo),
    constelacion = constelacion?.let { Constelacion(nombre = it.nombre, sobrenombre = it.sobrenombre) },
    etiquetas = etiquetas,
    resumen = resumen,
    exterior = exterior,
    ambientacion = ambientacion,
    narrativa = narrativa,
    restauracion = restauracion,
    imagenUrl = imagenUrl,
    salas = salas.sortedBy { it.orden }.map { it.aDominio() },
    mecanicas = mecanicas.map { it.aDominio() },
)

fun SalaResumenDto.aDominio() = SalaResumen(
    slug = slug,
    nombre = nombre,
    orden = orden,
    esZonaSegura = esZonaSegura,
    esObservatorio = esObservatorio,
)

fun MecanicaZonaDto.aDominio() = MecanicaZona(slug = slug, nombre = nombre, descripcion = descripcion)

fun SalaDto.aDominio() = Sala(
    slug = slug,
    nombre = nombre,
    descripcion = descripcion,
    aprendizaje = aprendizaje,
    esZonaSegura = esZonaSegura,
    imagenUrl = imagenUrl,
    zona = zona?.aDominio(),
    observatorio = observatorio?.aDominio(),
    archivos = archivos.map { it.aDominio() },
    dialogos = dialogos.map { it.aDominio() },
)

fun ObservatorioDto.aDominio() = Observatorio(
    nombre = nombre,
    exterior = exterior,
    interior = interior,
    luz = luz,
    descubrimientos = descubrimientos,
)

fun ArchivoEnSalaDto.aDominio() = ArchivoEnSala(slug = slug, titulo = titulo, categoria = categoria)
