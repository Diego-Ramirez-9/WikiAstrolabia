package com.equipo.wikiastrolabio.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Escala de espaciado con base de 4 dp. Usar solo estos valores para que todas las pantallas
 * tengan el mismo ritmo: xs dentro de un componente, m entre elementos de un bloque,
 * xl entre bloques y xxl entre secciones.
 */
object Espaciado {
    val xxs = 2.dp
    val xs = 4.dp
    val s = 8.dp
    val m = 12.dp
    val l = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
    val xxxl = 48.dp

    /** Margen lateral de todas las pantallas. */
    val margenPantalla = 20.dp

    /** Relleno interno de paneles y tarjetas. */
    val rellenoPanel = 16.dp

    /** Tamaño mínimo de cualquier elemento tocable (accesibilidad). */
    val objetivoTactil = 48.dp
}
