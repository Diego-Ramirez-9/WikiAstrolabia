package com.equipo.wikiastrolabio.ui.feature.inicio.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.ui.feature.inicio.SeccionCodex
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.Obsidian900
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate700
import com.equipo.wikiastrolabio.ui.theme.Slate800
import com.equipo.wikiastrolabio.ui.theme.TerraCotta500

/**
 * Renderiza los íconos pixel-art del códex de la pantalla Inicio.
 * Cada ícono está construido sobre una cuadrícula pixelada con los colores de Figma.
 */
@Composable
fun IconoCodexPixel(
    seccion: SeccionCodex,
    modifier: Modifier = Modifier,
    tamano: Dp = 32.dp,
) {
    val mod = modifier.size(tamano)
    when (seccion) {
        SeccionCodex.HISTORIA -> IconoHistoria(mod)
        SeccionCodex.MUNDO -> IconoMundo(mod)
        SeccionCodex.PERSONAJES -> IconoPersonajes(mod)
        SeccionCodex.MECANICAS -> IconoMecanicas(mod)
        SeccionCodex.ARCHIVOS -> IconoArchivos(mod)
    }
}

/** Reloj de arena pixel art */
@Composable
private fun IconoHistoria(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val px = size.width / 16f
        fun p(x: Int, y: Int, color: Color, w: Int = 1, h: Int = 1) {
            drawRect(color, Offset(x * px, y * px), Size(w * px, h * px))
        }

        // Base y tope de madera/cobre
        p(3, 1, TerraCotta500, 10, 2)
        p(4, 3, Ambar400, 8, 1)

        p(3, 13, TerraCotta500, 10, 2)
        p(4, 12, Ambar400, 8, 1)

        // Columnas laterales
        p(3, 3, TerraCotta500, 1, 10)
        p(12, 3, TerraCotta500, 1, 10)

        // Cristal superior
        p(4, 4, Cream200, 8, 1)
        p(5, 5, Cream200, 6, 1)
        p(6, 6, Cream200, 4, 1)
        p(7, 7, Cream200, 2, 1)

        // Arena superior
        p(5, 4, Sand300, 6, 1)
        p(6, 5, Ambar400, 4, 1)
        p(7, 6, Ambar400, 2, 1)

        // Centro (grano cayendo)
        p(7, 8, Sand300, 2, 1)

        // Cristal inferior
        p(7, 9, Cream200, 2, 1)
        p(6, 10, Cream200, 4, 1)
        p(5, 11, Cream200, 6, 1)

        // Arena acumulada abajo
        p(7, 10, Sand300, 2, 1)
        p(6, 11, Sand300, 4, 1)
        p(5, 12, Ambar400, 6, 1)
    }
}

/** Mapa cósmico enrollado / desplegado pixel art */
@Composable
private fun IconoMundo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val px = size.width / 16f
        fun p(x: Int, y: Int, color: Color, w: Int = 1, h: Int = 1) {
            drawRect(color, Offset(x * px, y * px), Size(w * px, h * px))
        }

        // Cuerpo del mapa de pergamino
        p(2, 2, Cream200, 12, 12)
        p(3, 3, Sand300, 10, 10)

        // Bordes quemados / sombras
        p(1, 3, TerraCotta500, 1, 10)
        p(14, 3, TerraCotta500, 1, 10)
        p(2, 1, TerraCotta500, 12, 1)
        p(2, 14, TerraCotta500, 12, 1)

        // Esquinas dobladas
        p(2, 2, Ambar400, 2, 2)
        p(12, 2, Ambar400, 2, 2)
        p(2, 12, Ambar400, 2, 2)
        p(12, 12, Ambar400, 2, 2)

        // Rutas y marcas en el mapa (líneas y cruz)
        p(4, 5, Slate700, 3, 1)
        p(6, 6, Slate700, 1, 3)
        p(7, 8, Slate700, 3, 1)

        // Cruz de destino 'X'
        p(10, 5, TerraCotta500, 1, 1)
        p(12, 5, TerraCotta500, 1, 1)
        p(11, 6, TerraCotta500, 1, 1)
        p(10, 7, TerraCotta500, 1, 1)
        p(12, 7, TerraCotta500, 1, 1)

        // Rosa náutica / estrella guía
        p(5, 10, Ambar400, 2, 2)
    }
}

/** Silueta / medalla de personaje pixel art */
@Composable
private fun IconoPersonajes(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val px = size.width / 16f
        fun p(x: Int, y: Int, color: Color, w: Int = 1, h: Int = 1) {
            drawRect(color, Offset(x * px, y * px), Size(w * px, h * px))
        }

        // Círculo exterior (medallón)
        p(5, 1, TerraCotta500, 6, 1)
        p(3, 2, TerraCotta500, 10, 1)
        p(2, 3, TerraCotta500, 12, 1)
        p(1, 4, TerraCotta500, 14, 8)
        p(2, 12, TerraCotta500, 12, 1)
        p(3, 13, TerraCotta500, 10, 1)
        p(5, 14, TerraCotta500, 6, 1)

        // Fondo del medallón
        p(4, 2, Sand300, 8, 1)
        p(3, 3, Sand300, 10, 1)
        p(2, 4, Sand300, 12, 8)
        p(3, 12, Sand300, 10, 1)
        p(4, 13, Sand300, 8, 1)

        // Cabeza / casco autómata
        p(6, 4, Cream200, 4, 4)
        p(7, 5, Slate800, 1, 1) // Ojo
        p(9, 5, Slate800, 1, 1) // Ojo

        // Cuello
        p(7, 8, Cream200, 2, 1)

        // Hombros / busto
        p(4, 9, Cream200, 8, 4)
        p(3, 11, Cream200, 10, 2)
    }
}

/** Control retro pixel art para Mecánicas */
@Composable
private fun IconoMecanicas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val px = size.width / 16f
        fun p(x: Int, y: Int, color: Color, w: Int = 1, h: Int = 1) {
            drawRect(color, Offset(x * px, y * px), Size(w * px, h * px))
        }

        // Cuerpo del mando retro
        p(2, 5, Sand300, 12, 6)
        p(1, 6, Sand300, 14, 5)
        p(2, 11, Sand300, 3, 3) // Agarre izquierdo
        p(11, 11, Sand300, 3, 3) // Agarre derecho

        // Borde superior
        p(3, 4, TerraCotta500, 10, 1)

        // D-Pad (Cruz direccional izquierda)
        p(4, 7, Slate800, 1, 3)
        p(3, 8, Slate800, 3, 1)

        // Botones de acción derecha
        p(11, 7, TerraCotta500, 2, 2)
        p(10, 9, Ambar400, 2, 2)

        // Botones select/start centrales
        p(7, 8, Slate700, 1, 1)
        p(8, 8, Slate700, 1, 1)
    }
}

/** Pergamino / manuscrito de Archivos pixel art */
@Composable
private fun IconoArchivos(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val px = size.width / 16f
        fun p(x: Int, y: Int, color: Color, w: Int = 1, h: Int = 1) {
            drawRect(color, Offset(x * px, y * px), Size(w * px, h * px))
        }

        // Rollo superior
        p(3, 1, TerraCotta500, 9, 2)
        p(2, 2, Ambar400, 2, 2)
        p(11, 2, Ambar400, 2, 2)

        // Cuerpo del pergamino
        p(3, 3, Cream200, 9, 10)
        p(4, 3, Sand300, 7, 10)

        // Líneas de texto antiguo
        p(5, 5, Slate700, 5, 1)
        p(5, 7, Slate700, 5, 1)
        p(5, 9, Slate700, 4, 1)
        p(5, 11, Slate700, 5, 1)

        // Rollo inferior enrollado
        p(3, 13, TerraCotta500, 10, 2)
        p(2, 13, Ambar400, 2, 2)
        p(12, 13, Ambar400, 2, 2)
    }
}
