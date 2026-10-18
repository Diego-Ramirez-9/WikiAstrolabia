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
    @param:StringRes val etiqueta: Int,
    @param:DrawableRes val icono: Int,
) {
    INICIO(Ruta.PestanaInicio, R.string.nav_inicio, R.drawable.ic_nav_inicio),
    MUNDO(Ruta.PestanaMundo, R.string.nav_mundo, R.drawable.ic_nav_mundo),
    ARCHIVOS(Ruta.PestanaArchivos, R.string.nav_archivos, R.drawable.ic_nav_archivos),
}

/** La pestaña está seleccionada si el destino actual vive dentro de su grafo. */
fun NavDestination?.perteneceA(destino: DestinoPrincipal): Boolean =
    this?.hierarchy?.any { it.hasRoute(destino.ruta::class) } == true

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
