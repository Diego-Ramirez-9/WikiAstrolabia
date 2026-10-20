package com.equipo.wikiastrolabio.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.equipo.wikiastrolabio.R

/**
 * Pestañas de la barra inferior. Para agregar, quitar o reordenar pestañas basta con cambiar
 * este enum y el grafo correspondiente en AstrolabioNavHost.
 */
enum class DestinoPrincipal(
    val ruta: Ruta,
    /** Primera pantalla de la pestaña (a la que se regresa al volver a tocarla). */
    val rutaInicial: Ruta,
    @param:StringRes val etiqueta: Int,
    @param:DrawableRes val icono: Int,
) {
    INICIO(Ruta.PestanaInicio, Ruta.Inicio, R.string.nav_inicio, R.drawable.ic_nav_inicio),
    MUNDO(Ruta.PestanaMundo, Ruta.Mundo, R.string.nav_mundo, R.drawable.ic_nav_mundo),
    ARCHIVOS(Ruta.PestanaArchivos, Ruta.Archivos, R.string.nav_archivos, R.drawable.ic_nav_archivos),
}

/** La pestaña está seleccionada si el destino actual vive dentro de su grafo. */
fun NavDestination?.perteneceA(destino: DestinoPrincipal): Boolean =
    this?.hierarchy?.any { it.hasRoute(destino.ruta::class) } == true

/**
 * Toque en la barra inferior: en otra pestaña, cambia a ella; en la pestaña actual, regresa a
 * su primera pantalla (como en la mayoría de las apps con pestañas).
 */
fun NavController.tocarPestana(destino: DestinoPrincipal) {
    if (currentDestination.perteneceA(destino)) {
        popBackStack(destino.rutaInicial, inclusive = false)
    } else {
        navegarAPestana(destino)
    }
}

/**
 * Cambia de pestaña conservando la pila de cada una (si estabas en una sala dentro de
 * Mundo, al volver a Mundo regresas a esa sala).
 */
fun NavController.navegarAPestana(destino: DestinoPrincipal) {
    navigate(destino.ruta) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
