package com.equipo.wikiastrolabio.ui.feature.inicio.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.ui.feature.inicio.SeccionCodex
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.Obsidian900
import com.equipo.wikiastrolabio.ui.theme.PixelifySans
import com.equipo.wikiastrolabio.ui.theme.Slate800
import com.equipo.wikiastrolabio.ui.theme.TerraCotta500

/**
 * Tarjeta de sección del códex en la pantalla de Inicio según el diseño de Figma.
 * Fondo Obsidian-900, borde Slate-800, recuadro para icono a la izquierda,
 * título en Terra-Cotta-500 y descripción en Cream-200 con Pixelify Sans.
 */
@Composable
fun TarjetaCodex(
    seccion: SeccionCodex,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(2.dp),
        color = Obsidian900,
        border = BorderStroke(1.dp, Slate800),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Contenedor del ícono pixel art
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Background)
                    .border(1.dp, Slate800, RoundedCornerShape(2.dp)),
                contentAlignment = Alignment.Center,
            ) {
                IconoCodexPixel(seccion = seccion, tamano = 34.dp)
            }

            // Textos: Título y Descripción con Pixelify Sans
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = titulo.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = PixelifySans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        letterSpacing = 1.2.sp,
                    ),
                    color = TerraCotta500,
                )
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = PixelifySans,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 0.2.sp,
                    ),
                    color = Cream200,
                )
            }
        }
    }
}
