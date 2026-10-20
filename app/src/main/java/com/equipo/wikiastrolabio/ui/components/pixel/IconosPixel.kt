package com.equipo.wikiastrolabio.ui.components.pixel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Arena300
import com.equipo.wikiastrolabio.ui.theme.Crema100
import com.equipo.wikiastrolabio.ui.theme.Niebla400
import com.equipo.wikiastrolabio.ui.theme.Noche700
import com.equipo.wikiastrolabio.ui.theme.Noche800
import com.equipo.wikiastrolabio.ui.theme.Terracota500

/*
 * Íconos pixel art dibujados en una cuadrícula de 16×16 (sin imágenes: escalan sin perder
 * nitidez y se tiñen con el acento de cada zona). Cuando la API traiga íconos reales
 * (iconoUrl), las pantallas los muestran en su lugar.
 */

/** Pincel para dibujar en la cuadrícula de 16×16: p(x, y, color, ancho, alto). */
class PincelPixel internal constructor(private val scope: DrawScope) {
    private val px = scope.size.width / 16f
    fun p(x: Int, y: Int, color: Color, w: Int = 1, h: Int = 1) =
        scope.drawRect(color, Offset(x * px, y * px), Size(w * px, h * px))
}

@Composable
fun IconoPixel(modifier: Modifier = Modifier, tamano: Dp = 24.dp, dibujo: PincelPixel.() -> Unit) {
    Canvas(modifier = modifier.size(tamano)) { PincelPixel(this).dibujo() }
}

/**
 * Dibuja un patrón de texto: '#' = color principal, 'a' = relleno, 'o' = detalle, '.' = vacío.
 * Permite crear íconos nuevos "a mano" escribiendo 16 filas de 16 caracteres.
 */
@Composable
fun IconoPatron(
    patron: List<String>,
    modifier: Modifier = Modifier,
    tamano: Dp = 24.dp,
    principal: Color = Ambar400,
    relleno: Color = principal.copy(alpha = 0.25f),
    detalle: Color = Crema100,
) {
    IconoPixel(modifier, tamano) {
        patron.forEachIndexed { y, fila ->
            fila.forEachIndexed { x, c ->
                when (c) {
                    '#' -> p(x, y, principal)
                    'a' -> p(x, y, relleno)
                    'o' -> p(x, y, detalle)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------
// Íconos de contenido
// ---------------------------------------------------------------------------------------

@Composable
fun IconoEngranaje(modifier: Modifier = Modifier, tamano: Dp = 24.dp, color: Color = Ambar400) =
    IconoPatron(ENGRANAJE, modifier, tamano, principal = color)

@Composable
fun IconoObservatorio(modifier: Modifier = Modifier, tamano: Dp = 24.dp, color: Color = Ambar400) =
    IconoPatron(OJO, modifier, tamano, principal = color)

@Composable
fun IconoZonaSegura(modifier: Modifier = Modifier, tamano: Dp = 24.dp, color: Color = Ambar400) =
    IconoPatron(ESCUDO, modifier, tamano, principal = color)

@Composable
fun IconoPoema(modifier: Modifier = Modifier, tamano: Dp = 24.dp, color: Color = Ambar400) =
    IconoPatron(PLUMA, modifier, tamano, principal = color, detalle = color.copy(alpha = 0.5f))

@Composable
fun IconoDialogo(modifier: Modifier = Modifier, tamano: Dp = 24.dp, color: Color = Ambar400) =
    IconoPatron(BOCADILLO, modifier, tamano, principal = color, relleno = color.copy(alpha = 0.15f), detalle = color)

@Composable
fun IconoIlustracion(modifier: Modifier = Modifier, tamano: Dp = 24.dp, color: Color = Niebla400) =
    IconoPatron(MARCO, modifier, tamano, principal = color, relleno = color.copy(alpha = 0.6f), detalle = color)

@Composable
fun IconoEstrellaGuia(modifier: Modifier = Modifier, tamano: Dp = 24.dp, color: Color = Ambar400) =
    IconoPatron(ESTRELLA4, modifier, tamano, principal = color.copy(alpha = 0.6f), detalle = color)

// ---------------------------------------------------------------------------------------
// Íconos del códex (diseño original del equipo de UI/UX)
// ---------------------------------------------------------------------------------------

/** Reloj de arena: Historia. */
@Composable
fun IconoHistoria(modifier: Modifier = Modifier, tamano: Dp = 32.dp) = IconoPixel(modifier, tamano) {
    p(3, 1, Terracota500, 10, 2); p(4, 3, Ambar400, 8, 1)
    p(3, 13, Terracota500, 10, 2); p(4, 12, Ambar400, 8, 1)
    p(3, 3, Terracota500, 1, 10); p(12, 3, Terracota500, 1, 10)
    p(4, 4, Crema100, 8, 1); p(5, 5, Crema100, 6, 1); p(6, 6, Crema100, 4, 1); p(7, 7, Crema100, 2, 1)
    p(5, 4, Arena300, 6, 1); p(6, 5, Ambar400, 4, 1); p(7, 6, Ambar400, 2, 1)
    p(7, 8, Arena300, 2, 1)
    p(7, 9, Crema100, 2, 1); p(6, 10, Crema100, 4, 1); p(5, 11, Crema100, 6, 1)
    p(7, 10, Arena300, 2, 1); p(6, 11, Arena300, 4, 1); p(5, 12, Ambar400, 6, 1)
}

/** Carta estelar: Mundo. */
@Composable
fun IconoMundo(modifier: Modifier = Modifier, tamano: Dp = 32.dp) = IconoPixel(modifier, tamano) {
    p(2, 2, Crema100, 12, 12); p(3, 3, Arena300, 10, 10)
    p(1, 3, Terracota500, 1, 10); p(14, 3, Terracota500, 1, 10)
    p(2, 1, Terracota500, 12, 1); p(2, 14, Terracota500, 12, 1)
    p(2, 2, Ambar400, 2, 2); p(12, 2, Ambar400, 2, 2); p(2, 12, Ambar400, 2, 2); p(12, 12, Ambar400, 2, 2)
    p(4, 5, Noche700, 3, 1); p(6, 6, Noche700, 1, 3); p(7, 8, Noche700, 3, 1)
    p(10, 5, Terracota500); p(12, 5, Terracota500); p(11, 6, Terracota500); p(10, 7, Terracota500); p(12, 7, Terracota500)
    p(5, 10, Ambar400, 2, 2)
}

/** Medallón con autómata: Personajes. */
@Composable
fun IconoPersonajes(modifier: Modifier = Modifier, tamano: Dp = 32.dp) = IconoPixel(modifier, tamano) {
    p(5, 1, Terracota500, 6, 1); p(3, 2, Terracota500, 10, 1); p(2, 3, Terracota500, 12, 1)
    p(1, 4, Terracota500, 14, 8); p(2, 12, Terracota500, 12, 1); p(3, 13, Terracota500, 10, 1); p(5, 14, Terracota500, 6, 1)
    p(4, 2, Arena300, 8, 1); p(3, 3, Arena300, 10, 1); p(2, 4, Arena300, 12, 8); p(3, 12, Arena300, 10, 1); p(4, 13, Arena300, 8, 1)
    p(6, 4, Crema100, 4, 4); p(7, 5, Noche800); p(9, 5, Noche800)
    p(7, 8, Crema100, 2, 1); p(4, 9, Crema100, 8, 4); p(3, 11, Crema100, 10, 2)
}

/** Mando: Mecánicas. */
@Composable
fun IconoMecanicas(modifier: Modifier = Modifier, tamano: Dp = 32.dp) = IconoPixel(modifier, tamano) {
    p(2, 5, Arena300, 12, 6); p(1, 6, Arena300, 14, 5); p(2, 11, Arena300, 3, 3); p(11, 11, Arena300, 3, 3)
    p(3, 4, Terracota500, 10, 1)
    p(4, 7, Noche800, 1, 3); p(3, 8, Noche800, 3, 1)
    p(11, 7, Terracota500, 2, 2); p(10, 9, Ambar400, 2, 2)
    p(7, 8, Noche700); p(8, 8, Noche700)
}

/** Pergamino: Archivos. */
@Composable
fun IconoArchivos(modifier: Modifier = Modifier, tamano: Dp = 32.dp) = IconoPixel(modifier, tamano) {
    p(3, 1, Terracota500, 9, 2); p(2, 2, Ambar400, 2, 2); p(11, 2, Ambar400, 2, 2)
    p(3, 3, Crema100, 9, 10); p(4, 3, Arena300, 7, 10)
    p(5, 5, Noche700, 5, 1); p(5, 7, Noche700, 5, 1); p(5, 9, Noche700, 4, 1); p(5, 11, Noche700, 5, 1)
    p(3, 13, Terracota500, 10, 2); p(2, 13, Ambar400, 2, 2); p(12, 13, Ambar400, 2, 2)
}

/** Estrella de los marcadores del mapa; [color] es el tinte de la zona. */
@Composable
fun EstrellaPixel(
    seleccionada: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    tamano: Dp = 24.dp,
) = IconoPixel(modifier, tamano) {
    val brillo = if (seleccionada) Crema100 else color.copy(alpha = 0.55f)
    val cuerpo = if (seleccionada) color else color.copy(alpha = 0.45f)
    val sombra = if (seleccionada) color.copy(alpha = 0.6f) else Noche700
    p(7, 1, sombra, 2, 1); p(7, 2, cuerpo, 2, 2); p(7, 3, brillo, 2, 1)
    p(6, 4, cuerpo, 4, 1); p(7, 4, brillo, 2, 1)
    p(2, 5, sombra, 12, 1); p(1, 6, sombra, 14, 1); p(2, 6, cuerpo, 12, 1); p(6, 5, brillo, 4, 2)
    p(3, 7, cuerpo, 10, 2); p(6, 7, brillo, 4, 2); p(4, 9, cuerpo, 8, 1); p(5, 9, brillo, 6, 1)
    p(3, 10, cuerpo, 4, 2); p(9, 10, cuerpo, 4, 2); p(2, 12, cuerpo, 3, 2); p(11, 12, cuerpo, 3, 2)
    p(2, 14, sombra, 3, 1); p(11, 14, sombra, 3, 1)
}

/** Esfera de reloj: marcador del desenlace (Cámara Central) y animación de carga. */
@Composable
fun RelojPixel(
    color: Color,
    modifier: Modifier = Modifier,
    tamano: Dp = 24.dp,
    manecillas: Color = Crema100,
) = IconoPixel(modifier, tamano) {
    p(5, 2, color, 6, 1); p(3, 3, color, 2, 1); p(11, 3, color, 2, 1)
    p(2, 4, color, 1, 2); p(13, 4, color, 1, 2); p(1, 6, color, 1, 4); p(14, 6, color, 1, 4)
    p(2, 10, color, 1, 2); p(13, 10, color, 1, 2); p(3, 12, color, 2, 1); p(11, 12, color, 2, 1); p(5, 13, color, 6, 1)
    p(7, 7, manecillas, 2, 2); p(7, 4, manecillas, 2, 3); p(9, 6, manecillas, 2, 2)
}

// ---------------------------------------------------------------------------------------
// Patrones ('#' principal, 'a' relleno, 'o' detalle)
// ---------------------------------------------------------------------------------------

private val ENGRANAJE = listOf(
    "................",
    ".......##.......",
    "...##.####.##...",
    "...##########...",
    "....###..###....",
    "..###......###..",
    ".####......####.",
    "..##........##..",
    "..##........##..",
    ".####......####.",
    "..###......###..",
    "....###..###....",
    "...##########...",
    "...##.####.##...",
    ".......##.......",
    "................",
)

private val OJO = listOf(
    "................",
    "................",
    "................",
    "......####......",
    "....##....##....",
    "..##..####..##..",
    ".#...##oo##...#.",
    "#....#oooo#....#",
    ".#...##oo##...#.",
    "..##..####..##..",
    "....##....##....",
    "......####......",
    "................",
    "................",
    "................",
    "................",
)

private val ESCUDO = listOf(
    "................",
    "..############..",
    "..#aaaaaaaaaa#..",
    "..#aaaaaaaaaa#..",
    "..#aaaaooaaaa#..",
    "..#aaaaooaaaa#..",
    "..#aaooooooaa#..",
    "..#aaooooooaa#..",
    "..#aaaaooaaaa#..",
    "...#aaaooaaa#...",
    "...#aaaaaaaa#...",
    "....#aaaaaa#....",
    ".....#aaaa#.....",
    "......#aa#......",
    ".......##.......",
    "................",
)

private val PLUMA = listOf(
    "................",
    "............###.",
    "...........#aa#.",
    "..........#aa#..",
    ".........#aa#...",
    "........#aa#....",
    ".......#aa#.....",
    "......#aa#......",
    ".....#aa#.......",
    "....#aa#........",
    "....#a#.........",
    "...##...........",
    "..#.............",
    ".#..............",
    "oooooo..........",
    "................",
)

private val BOCADILLO = listOf(
    "................",
    "................",
    "..############..",
    ".#aaaaaaaaaaaa#.",
    ".#aooaooooaooa#.",
    ".#aaaaaaaaaaaa#.",
    ".#aooooaoooaaa#.",
    ".#aaaaaaaaaaaa#.",
    "..#####..#####..",
    "......#.#.......",
    "......##........",
    "......#.........",
    "................",
    "................",
    "................",
    "................",
)

private val MARCO = listOf(
    "................",
    "................",
    ".##############.",
    ".#............#.",
    ".#.........oo.#.",
    ".#.........oo.#.",
    ".#............#.",
    ".#.....#......#.",
    ".#....###.....#.",
    ".#...#####.#..#.",
    ".#..#######aa.#.",
    ".#.##########a#.",
    ".##############.",
    "................",
    "................",
    "................",
)

private val ESTRELLA4 = listOf(
    "................",
    ".......##.......",
    ".......##.......",
    "......#oo#......",
    "......#oo#......",
    ".....#oooo#.....",
    "..###oooooo###..",
    "##oooooooooooo##",
    "##oooooooooooo##",
    "..###oooooo###..",
    ".....#oooo#.....",
    "......#oo#......",
    "......#oo#......",
    ".......##.......",
    ".......##.......",
    "................",
)
