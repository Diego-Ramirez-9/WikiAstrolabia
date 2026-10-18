package com.equipo.wikiastrolabio.data.remote

import com.equipo.wikiastrolabio.data.remote.dto.ArchivoDto
import com.equipo.wikiastrolabio.data.remote.dto.CategoriaArchivoDto
import com.equipo.wikiastrolabio.data.remote.dto.EntradaHistoriaDto
import com.equipo.wikiastrolabio.data.remote.dto.MapaDto
import com.equipo.wikiastrolabio.data.remote.dto.MecanicasDto
import com.equipo.wikiastrolabio.data.remote.dto.PersonajeDto
import com.equipo.wikiastrolabio.data.remote.dto.PersonajeResumenDto
import com.equipo.wikiastrolabio.data.remote.dto.RespuestaApi
import com.equipo.wikiastrolabio.data.remote.dto.SalaDto
import com.equipo.wikiastrolabio.data.remote.dto.SaludDto
import com.equipo.wikiastrolabio.data.remote.dto.SeccionHistoriaDto
import com.equipo.wikiastrolabio.data.remote.dto.VersionContenidoDto
import com.equipo.wikiastrolabio.data.remote.dto.ZonaDto
import com.equipo.wikiastrolabio.data.remote.dto.ZonaResumenDto
import com.equipo.wikiastrolabio.di.RedModule
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Valida las respuestas falsas de src/debug/assets/fake-api contra los DTO.
 * Si el backend o UI/UX cambian la forma de un JSON y estos tests fallan, hay que actualizar
 * los DTO o volver a generar los JSON (tools/fake-api/generar_fake_api.py).
 */
class FakeApiAssetsTest {

    private val json = RedModule.provideJson()

    // Las pruebas unitarias se ejecutan con el módulo app/ como carpeta de trabajo.
    private val carpeta = File("src/debug/assets/fake-api")

    private inline fun <reified T> leer(ruta: String): T =
        json.decodeFromString<RespuestaApi<T>>(File(carpeta, "$ruta.json").readText()).data

    private fun slugsEn(subcarpeta: String): List<String> =
        File(carpeta, subcarpeta).listFiles().orEmpty().map { it.nameWithoutExtension }

    @Test
    fun `las respuestas de listas se leen con sus DTO`() {
        assertTrue("No existe ${carpeta.absolutePath}", carpeta.isDirectory)
        leer<SaludDto>("salud")
        leer<VersionContenidoDto>("contenido/version")
        assertTrue(leer<List<SeccionHistoriaDto>>("historia").isNotEmpty())
        assertTrue(leer<MapaDto>("mapa").puntos.isNotEmpty())
        assertTrue(leer<List<ZonaResumenDto>>("zonas").isNotEmpty())
        assertTrue(leer<List<ZonaResumenDto>>("zonas.tipo-casa").all { it.tipo == "casa" })
        assertTrue(leer<List<PersonajeResumenDto>>("personajes").isNotEmpty())
        assertTrue(leer<MecanicasDto>("mecanicas").habilidades.isNotEmpty())
        assertTrue(leer<List<CategoriaArchivoDto>>("archivos").isNotEmpty())
    }

    @Test
    fun `los detalles se leen con sus DTO`() {
        slugsEn("historia").forEach { leer<EntradaHistoriaDto>("historia/$it") }
        slugsEn("zonas").forEach { leer<ZonaDto>("zonas/$it") }
        slugsEn("salas").forEach { leer<SalaDto>("salas/$it") }
        slugsEn("personajes").forEach { leer<PersonajeDto>("personajes/$it") }
        slugsEn("archivos").forEach { leer<ArchivoDto>("archivos/$it") }
    }

    @Test
    fun `cada enlace de las listas tiene su detalle`() {
        val faltantes = buildList {
            leer<List<SeccionHistoriaDto>>("historia").flatMap { it.entradas }.forEach { add("historia/${it.slug}") }
            leer<MapaDto>("mapa").puntos.forEach { add("zonas/${it.slug}") }
            slugsEn("zonas").flatMap { leer<ZonaDto>("zonas/$it").salas }.forEach { add("salas/${it.slug}") }
            leer<List<PersonajeResumenDto>>("personajes").forEach { add("personajes/${it.slug}") }
            leer<List<CategoriaArchivoDto>>("archivos").flatMap { it.elementos }.forEach { add("archivos/${it.slug}") }
        }.filterNot { File(carpeta, "$it.json").isFile }

        assertTrue("Faltan respuestas falsas: $faltantes", faltantes.isEmpty())
    }
}
