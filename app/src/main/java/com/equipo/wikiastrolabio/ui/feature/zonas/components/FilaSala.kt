package com.equipo.wikiastrolabio.ui.feature.zonas.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.domain.model.SalaResumen
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.FuentePixelFallback
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate800

/**
 * Fila de una habitación (sala) dentro de la lista de la Casa, rediseñada según Figma:
 * - Número de habitación en tipografía monospace/pixel (01, 02, 03...).
 * - Nombre de la habitación en color crema/blanco con tipografía pixel.
 * - Línea divisoria inferior en Slate-800.
 */
@Composable
fun FilaSala(
    sala: SalaResumen,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val nombreLimpio = sala.nombre
        .replaceFirst(Regex("^(El|La|Los|Las)\\s+", RegexOption.IGNORE_CASE), "")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Número orden 01, 02, etc.
            Text(
                text = sala.orden.toString().padStart(2, '0'),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = FuentePixelFallback,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    letterSpacing = 1.sp,
                ),
                color = Sand300,
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Nombre de la sala
            Text(
                text = nombreLimpio,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FuentePixelFallback,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.5.sp,
                    letterSpacing = 0.4.sp,
                ),
                color = Cream200,
                modifier = Modifier.weight(1f),
            )
        }

        HorizontalDivider(thickness = 1.dp, color = Slate800)
    }
}
