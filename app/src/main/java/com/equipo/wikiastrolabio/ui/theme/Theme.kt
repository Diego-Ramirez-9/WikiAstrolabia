package com.equipo.wikiastrolabio.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * La app solo tiene tema oscuro (estética cósmica del juego) y no usa "dynamic color":
 * en Android 12+ el color dinámico reemplazaría la paleta del juego con la del fondo de
 * pantalla del usuario.
 */
private val EsquemaAstrolabio = darkColorScheme(
    primary = TerraCotta500,
    onPrimary = Blanco,
    primaryContainer = Obsidian900,
    onPrimaryContainer = Ambar400,
    secondary = Sand300,
    onSecondary = Background,
    // Indicador de la pestaña seleccionada en la barra inferior.
    secondaryContainer = Slate800,
    onSecondaryContainer = Ambar400,
    tertiary = Ambar400,
    onTertiary = Negro,
    background = Background,
    onBackground = Cream200,
    surface = Background,
    onSurface = Cream200,
    surfaceVariant = Obsidian900,
    onSurfaceVariant = Sand300,
    surfaceContainer = Obsidian900,
    surfaceContainerHigh = Slate800,
    outline = Slate800,
    outlineVariant = Slate700,
    error = RojoAlerta,
    onError = Negro,
)

@Composable
fun WikiAstrolabioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaAstrolabio,
        typography = Typography,
        content = content,
    )
}
