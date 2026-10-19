package com.equipo.wikiastrolabio.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Tipografía de WikiAstrolabio.
 *
 * CÓMO IMPORTAR UNA TIPOGRAFÍA PIXEL ART (.ttf / .otf):
 * 1. Descarga la fuente que usas en Figma (ej. Silkscreen, Pixelify Sans, Press Start 2P, ThaleahFat, etc.).
 * 2. Cópiala en la carpeta: `app/src/main/res/font/`
 *    REGLA DE ANDROID: El nombre de archivo debe estar en MINÚSCULAS y guiones bajos (ej: `pixel_art.ttf`).
 * 3. Descomenta la línea de importación:
 *    ```kotlin
 *    import androidx.compose.ui.text.font.Font
 *    import com.equipo.wikiastrolabio.R
 *
 *    val FuentePixel = FontFamily(Font(R.font.pixel_art, FontWeight.Normal))
 *    ```
 * 4. Y sustituye `FuentePixelFallback` por `FuentePixel`.
 */
import androidx.compose.ui.text.font.Font
import com.equipo.wikiastrolabio.R

// Tipografía Pixelify Sans de Google Fonts
val PixelifySans = FontFamily(
    Font(R.font.pixelify_sans, FontWeight.Normal),
    Font(R.font.pixelify_sans, FontWeight.Medium),
    Font(R.font.pixelify_sans, FontWeight.SemiBold),
    Font(R.font.pixelify_sans, FontWeight.Bold),
    Font(R.font.pixelify_sans, FontWeight.ExtraBold),
)

val FuentePixelFallback = PixelifySans
val FuentePixelTitulosFallback = PixelifySans

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FuentePixelTitulosFallback,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        letterSpacing = 2.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FuentePixelTitulosFallback,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        letterSpacing = 1.8.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FuentePixelTitulosFallback,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = 1.2.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FuentePixelTitulosFallback,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FuentePixelFallback,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FuentePixelFallback,
        fontWeight = FontWeight.Normal,
        fontSize = 13.5.sp,
        lineHeight = 19.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FuentePixelFallback,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FuentePixelFallback,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        letterSpacing = 1.2.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FuentePixelFallback,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        letterSpacing = 1.2.sp,
    ),
)
