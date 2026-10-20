package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento

/**
 * Fila de un índice (salas de una zona, entradas de un capítulo, registros de una colección):
 * número en pixel, título, resumen opcional, insignias y flecha. Toda la fila es tocable.
 */
@Composable
fun FilaIndice(
    titulo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    numero: Int? = null,
    subtitulo: String? = null,
    lineasSubtitulo: Int = 2,
    inicio: (@Composable () -> Unit)? = null,
    insignias: (@Composable () -> Unit)? = null,
) {
    val acento = LocalAcento.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .clickable(onClick = onClick)
            .padding(vertical = Espaciado.m),
        horizontalArrangement = Arrangement.spacedBy(Espaciado.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (numero != null) {
            Text(
                text = numero.toString().padStart(2, '0'),
                style = MaterialTheme.typography.labelLarge,
                color = acento,
                modifier = Modifier.width(24.dp),
            )
        }
        inicio?.invoke()
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Espaciado.xs),
        ) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            if (!subtitulo.isNullOrBlank()) {
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = lineasSubtitulo,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            insignias?.let { FilaInsignias(modifier = Modifier.padding(top = Espaciado.xs)) { it() } }
        }
        Icon(
            painter = painterResource(R.drawable.ic_flecha_derecha),
            contentDescription = null,
            tint = acento.copy(alpha = 0.8f),
            modifier = Modifier.size(16.dp),
        )
    }
}

/** Enlace a un elemento vecino para seguir leyendo o recorriendo. */
data class Vecino(
    val etiqueta: String,
    val titulo: String,
    val onClick: () -> Unit,
)

/** Par de tarjetas "anterior / siguiente" al final de una lectura o de una sala. */
@Composable
fun NavegacionVecinos(
    anterior: Vecino?,
    siguiente: Vecino?,
    modifier: Modifier = Modifier,
) {
    if (anterior == null && siguiente == null) return
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Espaciado.xl),
        horizontalArrangement = Arrangement.spacedBy(Espaciado.m),
    ) {
        TarjetaVecino(anterior, haciaAtras = true, modifier = Modifier.weight(1f))
        TarjetaVecino(siguiente, haciaAtras = false, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun TarjetaVecino(vecino: Vecino?, haciaAtras: Boolean, modifier: Modifier) {
    if (vecino == null) {
        // Ocupa el espacio para que la otra tarjeta conserve su lado.
        Row(modifier) {}
        return
    }
    val acento = LocalAcento.current
    PanelPixel(modifier = modifier, onClick = vecino.onClick, separacion = Espaciado.xs) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Espaciado.xs, if (haciaAtras) Alignment.Start else Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (haciaAtras) Icon(painterResource(R.drawable.ic_atras), null, tint = acento, modifier = Modifier.size(14.dp))
            Text(vecino.etiqueta.uppercase(), style = MaterialTheme.typography.labelSmall, color = acento)
            if (!haciaAtras) Icon(painterResource(R.drawable.ic_flecha_derecha), null, tint = acento, modifier = Modifier.size(14.dp))
        }
        Text(
            text = vecino.titulo,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = if (haciaAtras) TextAlign.Start else TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
