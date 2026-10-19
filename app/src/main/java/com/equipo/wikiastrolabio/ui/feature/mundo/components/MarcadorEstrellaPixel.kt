package com.equipo.wikiastrolabio.ui.feature.mundo.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.MutedPurple600
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate700
import com.equipo.wikiastrolabio.ui.theme.Slate800
import com.equipo.wikiastrolabio.ui.theme.TerraCotta500

/**
 * Ícono de estrella pixel art para los marcadores del mapa del Gran Reloj.
 * Cambia a tonos dorados/ámbar brillantes cuando está seleccionada,
 * o tonos pizarra/gris cuando no está activa.
 */
@Composable
fun EstrellaPixel(
    seleccionada: Boolean,
    modifier: Modifier = Modifier,
    tamano: Dp = 26.dp,
) {
    Canvas(modifier = modifier.size(tamano)) {
        val px = size.width / 16f
        fun p(x: Int, y: Int, color: Color, w: Int = 1, h: Int = 1) {
            drawRect(color, Offset(x * px, y * px), Size(w * px, h * px))
        }

        val colorBrillo = if (seleccionada) Cream200 else Slate700
        val colorCuerpo = if (seleccionada) Ambar400 else MutedPurple600
        val colorSombra = if (seleccionada) TerraCotta500 else Slate800

        // Punta superior
        p(7, 1, colorSombra, 2, 1)
        p(7, 2, colorCuerpo, 2, 2)
        p(7, 3, colorBrillo, 2, 1)

        // Hombros superiores
        p(6, 4, colorCuerpo, 4, 1)
        p(7, 4, colorBrillo, 2, 1)

        // Brazos horizontales (izq y der)
        p(2, 5, colorSombra, 12, 1)
        p(1, 6, colorSombra, 14, 1)
        p(2, 6, colorCuerpo, 12, 1)
        p(6, 5, colorBrillo, 4, 2)

        // Centro de la estrella
        p(3, 7, colorCuerpo, 10, 2)
        p(6, 7, colorBrillo, 4, 2)
        p(4, 9, colorCuerpo, 8, 1)
        p(5, 9, colorBrillo, 6, 1)

        // Piernas inferiores
        p(3, 10, colorCuerpo, 4, 2)
        p(9, 10, colorCuerpo, 4, 2)
        p(2, 12, colorCuerpo, 3, 2)
        p(11, 12, colorCuerpo, 3, 2)

        // Sombras inferiores
        p(2, 14, colorSombra, 3, 1)
        p(11, 14, colorSombra, 3, 1)
    }
}

/**
 * Ícono de esfera/reloj central pixel art para el marcador 06 (Cámara Central / Eje).
 */
@Composable
fun RelojPixel(
    seleccionada: Boolean,
    modifier: Modifier = Modifier,
    tamano: Dp = 24.dp,
) {
    Canvas(modifier = modifier.size(tamano)) {
        val px = size.width / 16f
        fun p(x: Int, y: Int, color: Color, w: Int = 1, h: Int = 1) {
            drawRect(color, Offset(x * px, y * px), Size(w * px, h * px))
        }

        val colorBorde = if (seleccionada) Ambar400 else Cream200
        val colorManecillas = if (seleccionada) Sand300 else Cream200

        // Círculo exterior pixelado
        p(5, 2, colorBorde, 6, 1)
        p(3, 3, colorBorde, 2, 1)
        p(11, 3, colorBorde, 2, 1)
        p(2, 4, colorBorde, 1, 2)
        p(13, 4, colorBorde, 1, 2)
        p(1, 6, colorBorde, 1, 4)
        p(14, 6, colorBorde, 1, 4)
        p(2, 10, colorBorde, 1, 2)
        p(13, 10, colorBorde, 1, 2)
        p(3, 12, colorBorde, 2, 1)
        p(11, 12, colorBorde, 2, 1)
        p(5, 13, colorBorde, 6, 1)

        // Manecillas del reloj (a las 10:10 o 2:00)
        p(7, 7, colorManecillas, 2, 2) // Centro
        p(7, 4, colorManecillas, 2, 3) // Hacia las 12
        p(9, 6, colorManecillas, 2, 2) // Hacia las 2
    }
}
