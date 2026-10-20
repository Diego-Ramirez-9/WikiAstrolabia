package com.equipo.wikiastrolabio.domain.usecase

import com.equipo.wikiastrolabio.domain.model.EntradaHistoria
import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.OrigenDatos
import com.equipo.wikiastrolabio.domain.model.Referencia
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.Sala
import com.equipo.wikiastrolabio.domain.model.SalaResumen
import com.equipo.wikiastrolabio.domain.model.SeccionHistoria
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.domain.model.Zona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen
import com.equipo.wikiastrolabio.domain.repository.HistoriaRepository
import com.equipo.wikiastrolabio.domain.repository.MundoRepository
import com.equipo.wikiastrolabio.domain.usecase.historia.ObtenerEntradaHistoriaUseCase
import com.equipo.wikiastrolabio.domain.usecase.mundo.ObtenerSalaUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Lógica de "anterior / siguiente" de la Historia y del recorrido de salas. */
class RecorridosTest {

    private fun entrada(slug: String) = EntradaHistoria(slug, slug, "…", null)

    private val indice = listOf(
        SeccionHistoria("cap-1", "Capítulo uno", listOf(entrada("a"), entrada("b"))),
        SeccionHistoria("cap-2", "Capítulo dos", listOf(entrada("c"))),
    )

    private inner class HistoriaFalsa(private val conIndice: Boolean = true) : HistoriaRepository {
        override suspend fun obtenerHistoria(forzarRed: Boolean) =
            if (conIndice) Resultado.Exito(indice, OrigenDatos.RED) else Resultado.Error(ErrorDatos.SinConexion)

        override suspend fun obtenerEntrada(slug: String, forzarRed: Boolean) =
            Resultado.Exito(entrada(slug), OrigenDatos.CACHE)
    }

    @Test
    fun `una entrada conoce a sus vecinas aunque cambie de capitulo`() = runTest {
        val lectura = (ObtenerEntradaHistoriaUseCase(HistoriaFalsa())("b") as Resultado.Exito).datos

        assertEquals("a", lectura.anterior?.slug)
        assertEquals("c", lectura.siguiente?.slug)
        assertEquals(1, lectura.numeroCapitulo)
    }

    @Test
    fun `la ultima entrada no tiene siguiente y su capitulo es el dos`() = runTest {
        val lectura = (ObtenerEntradaHistoriaUseCase(HistoriaFalsa())("c") as Resultado.Exito).datos

        assertEquals("b", lectura.anterior?.slug)
        assertNull(lectura.siguiente)
        assertEquals(2, lectura.numeroCapitulo)
    }

    @Test
    fun `sin indice la entrada se muestra igual sin vecinas`() = runTest {
        val resultado = ObtenerEntradaHistoriaUseCase(HistoriaFalsa(conIndice = false))("b") as Resultado.Exito

        assertEquals("b", resultado.datos.entrada.slug)
        assertNull(resultado.datos.anterior)
        assertNull(resultado.datos.siguiente)
        assertNull(resultado.datos.numeroCapitulo)
        assertEquals(OrigenDatos.CACHE, resultado.origen)
    }

    private fun salaResumen(orden: Int) = SalaResumen("sala-$orden", "Sala $orden", orden, esZonaSegura = false, esObservatorio = false)

    private class MundoFalso(private val zona: Zona?) : MundoRepository {
        override suspend fun obtenerMapa(forzarRed: Boolean): Resultado<Mapa> = error("No se usa")
        override suspend fun obtenerZonas(tipo: TipoZona?, forzarRed: Boolean): Resultado<List<ZonaResumen>> = error("No se usa")
        override suspend fun obtenerZona(slug: String, forzarRed: Boolean) =
            zona?.let { Resultado.Exito(it, OrigenDatos.RED) } ?: Resultado.Error(ErrorDatos.SinConexion)

        override suspend fun obtenerSala(slug: String, forzarRed: Boolean) = Resultado.Exito(
            Sala(slug, slug, 2, null, null, false, null, Referencia("geminis", "Casa de Géminis"), null, emptyList(), emptyList()),
            OrigenDatos.RED,
        )
    }

    private fun zonaCon(salas: List<SalaResumen>) = Zona(
        slug = "geminis", nombre = "Casa de Géminis", subtitulo = null, tipo = TipoZona.CASA, constelacion = null,
        esNivel = true, esTutorial = false, etiquetas = emptyList(), resumen = null, tonoEmocional = null,
        ideaCentral = null, exterior = null, ambientacion = null, narrativa = null, restauracion = null,
        coleccionables = null, paleta = null, iluminacion = null, sensacionVisual = null, tinte = "violeta",
        imagenUrl = null, salas = salas, mecanicas = emptyList(),
    )

    @Test
    fun `una sala conoce a sus vecinas, el total y el tinte de su zona`() = runTest {
        val salas = (1..3).map(::salaResumen)
        val recorrido = (ObtenerSalaUseCase(MundoFalso(zonaCon(salas)))("sala-2") as Resultado.Exito).datos

        assertEquals("sala-1", recorrido.anterior?.slug)
        assertEquals("sala-3", recorrido.siguiente?.slug)
        assertEquals(3, recorrido.total)
        assertEquals("violeta", recorrido.tinteZona)
        assertEquals(TipoZona.CASA, recorrido.tipoZona)
    }

    @Test
    fun `si la zona no responde la sala se muestra sin recorrido`() = runTest {
        val recorrido = (ObtenerSalaUseCase(MundoFalso(zona = null))("sala-2") as Resultado.Exito).datos

        assertNull(recorrido.anterior)
        assertNull(recorrido.siguiente)
        assertEquals(0, recorrido.total)
    }
}
