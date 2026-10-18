package com.equipo.wikiastrolabio.ui.feature.personajes

import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import com.equipo.wikiastrolabio.domain.model.OrigenDatos
import com.equipo.wikiastrolabio.domain.model.Personaje
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.RolPersonaje
import com.equipo.wikiastrolabio.domain.repository.PersonajesRepository
import com.equipo.wikiastrolabio.domain.usecase.personajes.ObtenerPersonajesUseCase
import com.equipo.wikiastrolabio.testutil.MainDispatcherRule
import com.equipo.wikiastrolabio.ui.common.UiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PersonajesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val almanaque = PersonajeResumen("almanaque-xii", "Almanaque XII", RolPersonaje.PROTAGONISTA, null, null)

    /** Repositorio falso: devuelve en orden las respuestas de la lista y registra cada llamada. */
    private class RepositorioFalso(vararg respuestas: Resultado<List<PersonajeResumen>>) : PersonajesRepository {
        private val pendientes = ArrayDeque(respuestas.toList())
        val llamadas = mutableListOf<Boolean>()

        override suspend fun obtenerPersonajes(forzarRed: Boolean): Resultado<List<PersonajeResumen>> {
            llamadas += forzarRed
            return pendientes.removeFirst()
        }

        override suspend fun obtenerPersonaje(slug: String, forzarRed: Boolean): Resultado<Personaje> =
            error("No se usa en esta prueba")
    }

    private fun viewModel(repositorio: PersonajesRepository) =
        PersonajesViewModel(ObtenerPersonajesUseCase(repositorio))

    @Test
    fun `al crearse carga la lista`() {
        val repositorio = RepositorioFalso(Resultado.Exito(listOf(almanaque), OrigenDatos.RED))

        val vm = viewModel(repositorio)

        assertEquals(UiState.Exito(listOf(almanaque)), vm.uiState.value)
        assertEquals(listOf(false), repositorio.llamadas)
    }

    @Test
    fun `datos de la cache sin conexion se marcan para avisar al usuario`() {
        val vm = viewModel(RepositorioFalso(Resultado.Exito(listOf(almanaque), OrigenDatos.CACHE_SIN_CONEXION)))

        assertEquals(UiState.Exito(listOf(almanaque), sinConexion = true), vm.uiState.value)
    }

    @Test
    fun `un error se muestra y reintentar vuelve a pedir`() {
        val repositorio = RepositorioFalso(
            Resultado.Error(ErrorDatos.SinConexion),
            Resultado.Exito(listOf(almanaque), OrigenDatos.RED),
        )
        val vm = viewModel(repositorio)
        assertEquals(UiState.Error(ErrorDatos.SinConexion), vm.uiState.value)

        vm.reintentar()

        assertEquals(UiState.Exito(listOf(almanaque)), vm.uiState.value)
    }

    @Test
    fun `recargar fuerza la red`() {
        val repositorio = RepositorioFalso(
            Resultado.Exito(listOf(almanaque), OrigenDatos.CACHE),
            Resultado.Exito(listOf(almanaque), OrigenDatos.RED),
        )
        val vm = viewModel(repositorio)

        vm.recargar()

        assertEquals(listOf(false, true), repositorio.llamadas)
    }
}
