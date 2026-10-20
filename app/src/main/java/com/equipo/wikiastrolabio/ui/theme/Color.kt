package com.equipo.wikiastrolabio.ui.theme

import androidx.compose.ui.graphics.Color

/*
 * Paleta de WikiAstrolabio: "códex del Gran Reloj".
 *
 * Regla 60-30-10: 60 % neutrales azul nocturno (fondos y paneles), 30 % pergamino (texto) y
 * 10 % acentos. El ámbar es el ÚNICO color de interacción (selección, botones, enlaces); el
 * terracota marca títulos de sección. Los tintes de zona sustituyen al ámbar dentro de cada Casa.
 *
 * Las pantallas no usan estos valores directo: usan MaterialTheme.colorScheme (ver Theme.kt)
 * o LocalAcento para el color de la zona.
 */

// Neutrales: azul nocturno, del fondo hacia la superficie más elevada.
val Noche950 = Color(0xFF0A131C)
val Noche900 = Color(0xFF0F1B27)
val Noche850 = Color(0xFF15253A)
val Noche800 = Color(0xFF1D3046)
val Noche700 = Color(0xFF2B4560)
val Niebla400 = Color(0xFF8396AA)

// Pergamino: texto.
val Crema100 = Color(0xFFF6EBDA)
val Pergamino300 = Color(0xFFCDBEA8)

// Acentos.
val Ambar400 = Color(0xFFFFAA5E)
val Terracota500 = Color(0xFFD08159)
val Arena300 = Color(0xFFFFD4A3)
val Rojo400 = Color(0xFFE5776B)

// Tintes de zona (campo "tinteFondo" de la API). Los desconocidos usan Ambar400.
val TinteVioleta = Color(0xFFAE93E6)
val TinteTurquesa = Color(0xFF4FC6B6)
val TinteLaton = Color(0xFFD9B36C)
