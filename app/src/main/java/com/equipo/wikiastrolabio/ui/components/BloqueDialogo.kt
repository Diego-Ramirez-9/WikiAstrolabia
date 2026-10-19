package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.domain.model.Dialogo
import com.equipo.wikiastrolabio.ui.theme.Espaciado

/** Diálogo de Efeméride con sus líneas en orden (detalle de personaje y de sala). */
@Composable
fun BloqueDialogo(dialogo: Dialogo, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(4.dp))
            .padding(Espaciado.m),
        verticalArrangement = Arrangement.spacedBy(Espaciado.xs),
    ) {
        dialogo.titulo?.let {
            Text(it, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
        }
        dialogo.momento?.let {
            Text(it, style = MaterialTheme.typography.labelSmall, fontStyle = FontStyle.Italic)
        }
        dialogo.lineas.forEach { linea ->
            Text("«${linea.texto}»", style = MaterialTheme.typography.bodyMedium)
            val acotacion = listOfNotNull(linea.lugar, linea.indicacion).joinToString(" · ")
            if (acotacion.isNotEmpty()) {
                Text(
                    text = acotacion,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
