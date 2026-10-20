package com.equipo.wikiastrolabio.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.R

/*
 * Dos voces tipográficas:
 * - Pixelify Sans (fuente variable 400–700, res/font): títulos, etiquetas, números y botones.
 *   Es la voz del juego.
 * - Serif del sistema (Noto Serif en Android): todo el texto de lectura (lore, poemas,
 *   descripciones). El pixel art cansa en párrafos largos; la serif da el tono de códex antiguo.
 *
 * Escala modular 1.25 (tercera mayor): 11 · 12 · 13 · 15 · 19 · 24 · 30 · 37.
 * Usar siempre estos estilos (MaterialTheme.typography.x); no definir tamaños en las pantallas.
 */
private fun pixelify(peso: Int) = Font(
    resId = R.font.pixelify_sans,
    weight = FontWeight(peso),
    variationSettings = FontVariation.Settings(FontVariation.weight(peso)),
)

val PixelifySans = FontFamily(pixelify(400), pixelify(500), pixelify(600), pixelify(700))

private val Lectura = FontFamily.Serif

val Typography = Typography(
    // Título de la portada (Inicio).
    displayMedium = TextStyle(fontFamily = PixelifySans, fontWeight = FontWeight.Bold, fontSize = 37.sp, lineHeight = 44.sp, letterSpacing = 1.5.sp),
    // Títulos de pantalla.
    displaySmall = TextStyle(fontFamily = PixelifySans, fontWeight = FontWeight.Bold, fontSize = 30.sp, lineHeight = 36.sp, letterSpacing = 1.sp),
    headlineSmall = TextStyle(fontFamily = PixelifySans, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 30.sp, letterSpacing = 0.5.sp),
    // Títulos de bloque y de tarjeta.
    titleLarge = TextStyle(fontFamily = PixelifySans, fontWeight = FontWeight.SemiBold, fontSize = 19.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
    titleMedium = TextStyle(fontFamily = PixelifySans, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 20.sp, letterSpacing = 0.5.sp),
    titleSmall = TextStyle(fontFamily = PixelifySans, fontWeight = FontWeight.Medium, fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 0.5.sp),
    // Lectura.
    bodyLarge = TextStyle(fontFamily = Lectura, fontSize = 16.sp, lineHeight = 26.sp),
    bodyMedium = TextStyle(fontFamily = Lectura, fontSize = 15.sp, lineHeight = 23.sp),
    bodySmall = TextStyle(fontFamily = Lectura, fontSize = 13.sp, lineHeight = 19.sp),
    // Botones, etiquetas en versalitas y chips.
    labelLarge = TextStyle(fontFamily = PixelifySans, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 16.sp, letterSpacing = 1.sp),
    labelMedium = TextStyle(fontFamily = PixelifySans, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 1.5.sp),
    labelSmall = TextStyle(fontFamily = PixelifySans, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 14.sp, letterSpacing = 1.sp),
)
