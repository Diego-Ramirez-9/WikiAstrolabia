package com.equipo.wikiastrolabio.data.remote

import com.equipo.wikiastrolabio.data.mapper.aDominio
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
import com.equipo.wikiastrolabio.domain.model.TipoZona
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Contrato con la API real: lee las respuestas guardadas en src/test/resources/api-v1 (se
 * actualizan con tools/api/actualizar_respuestas_prueba.py) y comprueba que los DTO las
 * entienden, que los mappers no fallan y que cada enlace de una lista tiene su detalle.
 * Si el equipo de la API cambia un endpoint, este test avisa antes de que falle la app.
 */
class ContratoApiTest {

    private val json = RedModule.provideJson()
    private val carpeta = File(requireNotNull(javaClass.getResource("/api-v1")) { "Faltan las respuestas de prueba" }.toURI())

    private inline fun <reified T> leer(ruta: String): T =
        json.decodeFromString<RespuestaApi<T>>(File(carpeta, "$ruta.json").readText()).data

    private fun slugsEn(subcarpeta: String): List<String> =
        File(carpeta, subcarpeta).listFiles().orEmpty().map { it.nameWithoutExtension }.sorted()

    @Test
    fun `los listados se leen y se convierten a dominio`() {
        assertEquals("ok", leer<SaludDto>("salud").estado)
        assertTrue(leer<VersionContenidoDto>("contenido/version").actualizadoEn.isNotBlank())
        assertTrue(leer<List<SeccionHistoriaDto>>("historia").map { it.aDominio() }.isNotEmpty())
        assertTrue(leer<MapaDto>("mapa").aDominio().puntos.isNotEmpty())
        assertTrue(leer<List<ZonaResumenDto>>("zonas").map { it.aDominio() }.isNotEmpty())
        assertTrue(leer<List<PersonajeResumenDto>>("personajes").map { it.aDominio() }.isNotEmpty())
        assertTrue(leer<MecanicasDto>("mecanicas").aDominio().jugables.isNotEmpty())
        assertTrue(leer<List<CategoriaArchivoDto>>("archivos").map { it.aDominio() }.isNotEmpty())
    }

    @Test
    fun `el filtro por tipo de zona devuelve solo ese tipo`() {
        val casas = leer<List<ZonaResumenDto>>("zonas.tipo-casa").map { it.aDominio() }
        assertTrue(casas.isNotEmpty())
        assertTrue(casas.all { it.tipo == TipoZona.CASA })
    }

    @Test
    fun `los detalles se leen y se convierten a dominio`() {
        slugsEn("historia").forEach { leer<EntradaHistoriaDto>("historia/$it").aDominio() }
        slugsEn("zonas").forEach { leer<ZonaDto>("zonas/$it").aDominio() }
        slugsEn("salas").forEach { leer<SalaDto>("salas/$it").aDominio() }
        slugsEn("personajes").forEach { leer<PersonajeDto>("personajes/$it").aDominio() }
        slugsEn("archivos").forEach { leer<ArchivoDto>("archivos/$it").aDominio() }
    }

    @Test
    fun `los campos nuevos de la API llegan al dominio`() {
        val geminis = leer<ZonaDto>("zonas/geminis").aDominio()
        assertEquals("violeta", geminis.tinte)
        assertTrue(!geminis.paleta.isNullOrBlank())

        val observatorio = slugsEn("salas")
            .map { leer<SalaDto>("salas/$it").aDominio() }
            .first { it.observatorio != null }
        assertNotEquals(0, observatorio.orden)
        assertTrue(!observatorio.observatorio?.contenidoBreve.isNullOrBlank())

        val dialogos = leer<PersonajeDto>("personajes/efemeride").aDominio().dialogos
        assertTrue(dialogos.isNotEmpty())
        assertTrue(dialogos.all { it.personaje?.slug == "efemeride" })
    }

    @Test
    fun `cada enlace de una lista tiene su detalle`() {
        val faltantes = buildList {
            leer<List<SeccionHistoriaDto>>("historia").flatMap { it.entradas }.forEach { add("historia/${it.slug}") }
            leer<MapaDto>("mapa").puntos.forEach { add("zonas/${it.slug}") }
            slugsEn("zonas").flatMap { leer<ZonaDto>("zonas/$it").salas }.forEach { add("salas/${it.slug}") }
            leer<List<PersonajeResumenDto>>("personajes").forEach { add("personajes/${it.slug}") }
            leer<List<CategoriaArchivoDto>>("archivos").flatMap { it.elementos }.forEach { add("archivos/${it.slug}") }
        }.filterNot { File(carpeta, "$it.json").isFile }

        assertTrue("Faltan respuestas de prueba: $faltantes", faltantes.isEmpty())
    }
}
