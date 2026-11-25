package com.equipo.wikiastrolabio.ui.feature.resenas

import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import com.equipo.wikiastrolabio.domain.model.NuevaResena
import com.equipo.wikiastrolabio.domain.model.OrigenDatos
import com.equipo.wikiastrolabio.domain.model.Resena
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.ResumenResenas
import com.equipo.wikiastrolabio.domain.repository.ResenasRepository
import com.equipo.wikiastrolabio.domain.usecase.resenas.ObtenerResenasUseCase
import com.equipo.wikiastrolabio.domain.usecase.resenas.PublicarResenaUseCase
import com.equipo.wikiastrolabio.testutil.MainDispatcherRule
import com.equipo.wikiastrolabio.ui.common.UiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ResenasViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val resena = Resena("a1", "Viajera", 5, "Me encantó la historia.", null)
    private val resumen = ResumenResenas(total = 1, promedio = 5.0, resenas = listOf(resena))

    private class RepositorioFalso(
        var lista: Resultado<ResumenResenas>,
        var respuestaPublicar: Resultado<Resena>? = null,
        val apodo: String? = null,
    ) : ResenasRepository {
        var consultas = 0
        val publicadas = mutableListOf<NuevaResena>()

        override suspend fun obtenerResenas(): Resultado<ResumenResenas> {
            consultas++
            return lista
        }

        override suspend fun publicar(resena: NuevaResena): Resultado<Resena> {
            publicadas += resena
            return requireNotNull(respuestaPublicar)
        }

        override suspend fun apodoGuardado(): String? = apodo
    }

    private fun viewModel(repositorio: ResenasRepository) = ResenasViewModel(
        ObtenerResenasUseCase(repositorio),
        PublicarResenaUseCase(repositorio),
    )

    private fun ResenasViewModel.llenar(apodo: String = "Viajera", estrellas: Int = 4, texto: String = "Un juego precioso.") {
        cambiarApodo(apodo)
        cambiarCalificacion(estrellas)
        cambiarTexto(texto)
    }

    @Test
    fun `al crearse carga las resenas y rellena el apodo guardado`() {
        val vm = viewModel(RepositorioFalso(Resultado.Exito(resumen, OrigenDatos.RED), apodo = "Engranaje"))

        assertEquals(UiState.Exito(resumen), vm.uiState.value)
        assertEquals("Engranaje", vm.formulario.value.apodo)
    }

    @Test
    fun `el formulario solo se puede enviar si cumple las reglas`() {
        val vm = viewModel(RepositorioFalso(Resultado.Exito(resumen, OrigenDatos.RED)))
        assertFalse(vm.formulario.value.puedeEnviar)

        vm.llenar(texto = "corto")
        assertFalse(vm.formulario.value.puedeEnviar)

        vm.cambiarTexto("Ahora sí tiene suficientes caracteres.")
        assertTrue(vm.formulario.value.puedeEnviar)

        vm.cambiarCalificacion(0)
        assertFalse(vm.formulario.value.puedeEnviar)
    }

    @Test
    fun `publicar envia la resena, limpia el formulario y recarga la lista`() {
        val publicada = resena.copy(id = "b2", apodo = "Viajera")
        val repositorio = RepositorioFalso(Resultado.Exito(resumen, OrigenDatos.RED), Resultado.Exito(publicada, OrigenDatos.RED))
        val vm = viewModel(repositorio)
        vm.llenar()

        vm.publicar()

        assertEquals(listOf(NuevaResena("Viajera", 4, "Un juego precioso.")), repositorio.publicadas)
        assertEquals(FormularioResena(apodo = "Viajera", publicada = true), vm.formulario.value)
        assertEquals(2, repositorio.consultas)
    }

    @Test
    fun `un rechazo de la API se muestra y se borra al editar`() {
        val rechazo = ErrorDatos.Rechazado(429, "Ya publicaste una reseña hace poco.")
        val repositorio = RepositorioFalso(Resultado.Exito(resumen, OrigenDatos.RED), Resultado.Error(rechazo))
        val vm = viewModel(repositorio)
        vm.llenar()

        vm.publicar()

        assertEquals(rechazo, vm.formulario.value.error)
        assertFalse(vm.formulario.value.enviando)
        assertEquals("Un juego precioso.", vm.formulario.value.texto)

        vm.cambiarTexto("Un juego precioso, de verdad.")
        assertEquals(null, vm.formulario.value.error)
    }
}
