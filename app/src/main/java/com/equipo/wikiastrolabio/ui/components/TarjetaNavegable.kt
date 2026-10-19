package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.FuentePixelFallback
import com.equipo.wikiastrolabio.ui.theme.FuentePixelTitulosFallback
import com.equipo.wikiastrolabio.ui.theme.Obsidian900
import com.equipo.wikiastrolabio.ui.theme.Slate800
import com.equipo.wikiastrolabio.ui.theme.TerraCotta500
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/**
 * Tarjeta tocable con título, texto secundario opcional y un elemento a la izquierda
 * adaptada a la paleta retro de Figma (Obsidian-900, Slate-800, TerraCotta-500).
 */
@Composable
fun TarjetaNavegable(
    titulo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    lineasSubtitulo: Int = 2,
    inicio: (@Composable () -> Unit)? = null,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, Slate800),
        colors = CardDefaults.cardColors(containerColor = Obsidian900),
    ) {
        Row(
            modifier = Modifier.padding(Espaciado.m),
            horizontalArrangement = Arrangement.spacedBy(Espaciado.m),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            inicio?.invoke()
            Column(verticalArrangement = Arrangement.spacedBy(Espaciado.xs)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FuentePixelTitulosFallback,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp,
                    ),
                    color = TerraCotta500,
                )
                if (!subtitulo.isNullOrBlank()) {
                    Text(
                        text = subtitulo,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FuentePixelFallback,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                        ),
                        color = Cream200,
                        maxLines = lineasSubtitulo,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TarjetaNavegablePreview() {
    WikiAstrolabioTheme {
        TarjetaNavegable(
            titulo = "Historia",
            subtitulo = "Descubre el origen del Gran Reloj.",
            onClick = {},
        )
    }
}
