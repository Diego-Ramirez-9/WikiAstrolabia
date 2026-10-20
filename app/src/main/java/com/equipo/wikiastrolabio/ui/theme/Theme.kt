package com.equipo.wikiastrolabio.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Solo tema oscuro (estética del juego) y sin "dynamic color": en Android 12+ el color
 * dinámico reemplazaría la paleta del juego con la del fondo de pantalla del usuario.
 */
private val EsquemaAstrolabio = darkColorScheme(
    primary = Ambar400,
    onPrimary = Noche950,
    primaryContainer = Noche850,
    onPrimaryContainer = Ambar400,
    secondary = Terracota500,
    onSecondary = Noche950,
    secondaryContainer = Noche850,
    onSecondaryContainer = Ambar400,
    tertiary = Arena300,
    onTertiary = Noche950,
    background = Noche950,
    onBackground = Crema100,
    surface = Noche950,
    onSurface = Crema100,
    surfaceVariant = Noche900,
    onSurfaceVariant = Pergamino300,
    surfaceContainerLowest = Noche950,
    surfaceContainerLow = Noche900,
    surfaceContainer = Noche900,
    surfaceContainerHigh = Noche850,
    surfaceContainerHighest = Noche800,
    outline = Noche700,
    outlineVariant = Noche800,
    error = Rojo400,
    onError = Noche950,
    inverseSurface = Crema100,
    inverseOnSurface = Noche950,
    scrim = Noche950,
)

/**
 * Esquinas cortadas en lugar de redondeadas: imitan el escalón de un píxel y dan el aspecto
 * de pieza de relojería. small = chips e insignias, medium = paneles, large = ilustraciones.
 */
private val FormasAstrolabio = Shapes(
    extraSmall = CutCornerShape(3.dp),
    small = CutCornerShape(4.dp),
    medium = CutCornerShape(6.dp),
    large = CutCornerShape(10.dp),
    extraLarge = CutCornerShape(14.dp),
)

/**
 * Color de acento del contexto actual. Fuera de una zona es el ámbar; dentro de una Casa es
 * su tinte (violeta en Géminis, turquesa en Acuario…). Los componentes lo leen con
 * `LocalAcento.current` para que toda la pantalla de la zona se tiña sola.
 */
val LocalAcento = staticCompositionLocalOf { Ambar400 }

@Composable
fun ConAcento(acento: Color, contenido: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAcento provides acento, content = contenido)
}

/** Convierte el campo "tinteFondo" de la API en un color; los desconocidos usan el ámbar. */
fun colorDeTinte(tinte: String?): Color = when (tinte?.trim()?.lowercase()) {
    "violeta", "morado", "purpura", "púrpura" -> TinteVioleta
    "turquesa", "aguamarina", "cian" -> TinteTurquesa
    "laton", "latón", "dorado", "oro" -> TinteLaton
    else -> Ambar400
}

@Composable
fun WikiAstrolabioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaAstrolabio,
        typography = Typography,
        shapes = FormasAstrolabio,
    ) {
        ConAcento(Ambar400, content)
    }
}
