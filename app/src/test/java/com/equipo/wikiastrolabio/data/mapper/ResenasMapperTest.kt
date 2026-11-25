package com.equipo.wikiastrolabio.data.mapper

import com.equipo.wikiastrolabio.data.remote.dto.ResenaDto
import com.equipo.wikiastrolabio.domain.model.ReglasResena
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class ResenasMapperTest {

    private fun dto(id: String, fecha: String? = "2026-11-25T17:45:31.120Z") =
        ResenaDto(id = id, apodo = "Viajera", calificacion = 5, texto = "Me encantó.", creadoEn = fecha)

    @Test
    fun `la fecha se lee en ISO y una invalida no rompe la resena`() {
        assertEquals(Instant.parse("2026-11-25T17:45:31.120Z"), dto("a").aDominio().creadoEn)
        assertNull(dto("a", fecha = "ayer").aDominio().creadoEn)
    }

    @Test
    fun `las reglas cuentan caracteres reales sin los espacios de los extremos`() {
        assertEquals(5, ReglasResena.longitud("  🌟🌟🌟🌟🌟  "))
        assertTrue(ReglasResena.esValida("Vi", 1, "🌟".repeat(10)))
        assertFalse(ReglasResena.esValida(" V ", 5, "Un juego precioso."))
        assertFalse(ReglasResena.esValida("Viajera", 6, "Un juego precioso."))
        assertFalse(ReglasResena.esValida("Viajera", 5, "x".repeat(501)))
    }
}
