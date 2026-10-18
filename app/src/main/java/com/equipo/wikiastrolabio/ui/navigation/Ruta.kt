package com.equipo.wikiastrolabio.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Destinos de la app (Navigation Compose con rutas tipadas).
 * Los argumentos son propiedades: `navController.navigate(Ruta.PersonajeDetalle("almanaque-xii"))`.
 * El ViewModel los lee con `savedStateHandle.toRoute<Ruta.PersonajeDetalle>()`.
 *
 * Para agregar una pantalla: declara aquí su ruta, regístrala en AstrolabioNavHost dentro de
 * la pestaña a la que pertenece y crea su carpeta en ui/feature/.
 */
sealed interface Ruta {

    // --- Pestañas de la barra inferior (cada una es un grafo con su propia pila) ---

    @Serializable
    data object PestanaInicio : Ruta

    @Serializable
    data object PestanaMundo : Ruta

    @Serializable
    data object PestanaArchivos : Ruta

    // --- Pestaña Inicio ---

    @Serializable
    data object Inicio : Ruta

    @Serializable
    data object Historia : Ruta

    @Serializable
    data class EntradaHistoriaDetalle(val slug: String) : Ruta

    @Serializable
    data object Personajes : Ruta

    @Serializable
    data class PersonajeDetalle(val slug: String) : Ruta

    @Serializable
    data object Mecanicas : Ruta

    // --- Pestaña Mundo ---

    @Serializable
    data object Mundo : Ruta

    @Serializable
    data class ZonaDetalle(val slug: String) : Ruta

    @Serializable
    data class SalaDetalle(val slug: String) : Ruta

    // --- Pestaña Archivos ---

    @Serializable
    data object Archivos : Ruta

    @Serializable
    data class ArchivoDetalle(val slug: String) : Ruta
}
