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
    primary = Cobre,
    onPrimary = AzulNoche,
    primaryContainer = CobreOscuro,
    onPrimaryContainer = Cobre,
    secondary = GrisNiebla,
    onSecondary = AzulNoche,
    // Indicador de la pestaña seleccionada en la barra inferior.
    secondaryContainer = CobreOscuro,
    onSecondaryContainer = Cobre,
    background = AzulNoche,
    onBackground = Pergamino,
    surface = AzulNoche,
    onSurface = Pergamino,
    surfaceVariant = AzulPanel,
    onSurfaceVariant = GrisNiebla,
    surfaceContainer = AzulPanel,
    surfaceContainerHigh = AzulPanelElevado,
    outline = AzulBorde,
    outlineVariant = AzulBorde,
    error = RojoAlerta,
    onError = AzulNoche,
)

@Composable
fun WikiAstrolabioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaAstrolabio,
        typography = Typography,
        content = content,
    )
}
