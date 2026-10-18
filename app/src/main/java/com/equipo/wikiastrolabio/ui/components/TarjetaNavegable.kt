package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/**
 * Tarjeta tocable con título, texto secundario opcional y un elemento a la izquierda
 * (ícono, número, miniatura). Se usa en Inicio, Historia, Personajes, Zonas y Archivos.
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
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
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                if (!subtitulo.isNullOrBlank()) {
                    Text(
                        text = subtitulo,
                        style = MaterialTheme.typography.bodySmall,
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
