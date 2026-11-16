package com.equipo.wikiastrolabio.domain.usecase

import com.equipo.wikiastrolabio.domain.model.EntradaHistoria
import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import com.equipo.wikiastrolabio.domain.model.OrigenDatos
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.SeccionHistoria
import com.equipo.wikiastrolabio.domain.repository.HistoriaRepository
import com.equipo.wikiastrolabio.domain.usecase.historia.ObtenerEntradaHistoriaUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Lógica de "anterior / siguiente" de la Historia. */
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
}
