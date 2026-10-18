package com.equipo.wikiastrolabio.data.mapper

import com.equipo.wikiastrolabio.data.remote.dto.HabilidadDto
import com.equipo.wikiastrolabio.data.remote.dto.PersonajeDto
import com.equipo.wikiastrolabio.data.remote.dto.PuntoMapaDto
import com.equipo.wikiastrolabio.data.remote.dto.RasgoDto
import com.equipo.wikiastrolabio.domain.model.EstadoPersonaje
import com.equipo.wikiastrolabio.domain.model.RolPersonaje
import com.equipo.wikiastrolabio.domain.model.TipoHabilidad
import com.equipo.wikiastrolabio.domain.model.TipoZona
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {

    @Test
    fun `los parametros jsonb se convierten a texto`() {
        val parametros = Json.parseToJsonElement(
            """{"duracion_s": 5, "recarga": "suelo firme", "superficies": ["latón", "piedra"]}"""
        ).jsonObject

        val habilidad = habilidad(parametros).aDominio()

        assertEquals(
            mapOf("duracion_s" to "5", "recarga" to "suelo firme", "superficies" to "latón, piedra"),
            habilidad.parametros,
        )
        assertEquals(TipoHabilidad.MECANICA, habilidad.tipo)
    }

    @Test
    fun `valores desconocidos de la API no rompen la app`() {
        assertEquals(TipoZona.DESCONOCIDO, tipoZonaDesdeApi("satelite"))
        assertEquals(RolPersonaje.DESCONOCIDO, rolDesdeApi(null))
        assertEquals(EstadoPersonaje.DESCONOCIDO, estadoDesdeApi("reparado"))
        assertEquals(TipoHabilidad.DESCONOCIDO, tipoHabilidadDesdeApi(""))
    }

    @Test
    fun `el filtro de zonas usa los mismos valores que la API`() {
        TipoZona.entries.filter { it != TipoZona.DESCONOCIDO }.forEach { tipo ->
            assertEquals(tipo, tipoZonaDesdeApi(tipo.valorApi))
        }
    }

    @Test
    fun `las coordenadas del mapa se acotan entre 0 y 1`() {
        val punto = PuntoMapaDto(slug = "x", nombre = "X", tipo = "casa", x = 1.4f, y = -0.2f).aDominio()

        assertEquals(1f, punto.x)
        assertEquals(0f, punto.y)
    }

    @Test
    fun `los rasgos conservan el orden de la API y omiten categorias vacias`() {
        val personaje = PersonajeDto(
            slug = "efemeride",
            nombre = "Efeméride",
            rol = "guia",
            rasgos = linkedMapOf(
                "presencia" to listOf(RasgoDto("Voz", "Serena")),
                "apariencia" to emptyList(),
                "comunicacion" to listOf(RasgoDto("Tono", "Cálido")),
            ),
        ).aDominio()

        assertEquals(listOf("presencia", "comunicacion"), personaje.rasgos.map { it.categoria })
        assertEquals(RolPersonaje.GUIA, personaje.rol)
        assertEquals(EstadoPersonaje.ACTIVO, personaje.estado)
    }

    private fun habilidad(parametros: JsonObject) = HabilidadDto(
        slug = "gancho",
        nombreCorto = "Gancho",
        nombre = "Gancho",
        tipo = "mecanica",
        descripcion = "…",
        parametros = parametros,
    )
}
