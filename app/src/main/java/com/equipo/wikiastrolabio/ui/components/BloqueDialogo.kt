package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Dialogo
import com.equipo.wikiastrolabio.domain.model.TipoDialogo
import com.equipo.wikiastrolabio.ui.components.pixel.IconoDialogo
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/**
 * Diálogo de Efeméride como transcripción: título, tipo, momento del juego, quién habla y
 * dónde ocurre (con enlaces) y las líneas con sus acotaciones de cámara o sonido.
 */
@Composable
fun BloqueDialogo(
    dialogo: Dialogo,
    modifier: Modifier = Modifier,
    onPersonajeClick: ((slug: String) -> Unit)? = null,
    onSalaClick: ((slug: String) -> Unit)? = null,
) {
    val acento = LocalAcento.current
    PanelPixel(modifier = modifier, separacion = Espaciado.m) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Espaciado.m),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconoDialogo(tamano = 28.dp, color = acento)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Espaciado.xs)) {
                Text(
                    text = dialogo.titulo ?: stringResource(R.string.dialogo_sin_titulo),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                textoTipo(dialogo.tipo)?.let { Insignia(it) }
            }
        }
        dialogo.momento?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        val personaje = dialogo.personaje
        val sala = dialogo.sala
        if ((personaje != null && onPersonajeClick != null) || (sala != null && onSalaClick != null)) {
            FilaInsignias {
                if (personaje != null && onPersonajeClick != null) {
                    ChipEnlace(stringResource(R.string.dialogo_habla, personaje.nombre), onClick = { onPersonajeClick(personaje.slug) })
                }
                if (sala != null && onSalaClick != null) {
                    ChipEnlace(stringResource(R.string.dialogo_ocurre_en, sala.nombre), onClick = { onSalaClick(sala.slug) })
                }
            }
        }

        Divisor()
        dialogo.lineas.forEach { linea ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(Espaciado.m),
            ) {
                Box(
                    Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(acento.copy(alpha = 0.45f)),
                )
                Column(verticalArrangement = Arrangement.spacedBy(Espaciado.xs)) {
                    Text(
                        text = "«${linea.texto}»",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    val acotacion = listOfNotNull(linea.lugar, linea.indicacion).joinToString(" · ")
                    if (acotacion.isNotEmpty()) {
                        Text(
                            text = acotacion.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun textoTipo(tipo: TipoDialogo): String? = when (tipo) {
    TipoDialogo.ESCENA -> stringResource(R.string.dialogo_escena)
    TipoDialogo.INDICACION -> stringResource(R.string.dialogo_indicacion)
    TipoDialogo.CITA -> stringResource(R.string.dialogo_cita)
    TipoDialogo.DESCONOCIDO -> null
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C)
@Composable
private fun BloqueDialogoPreview() {
    WikiAstrolabioTheme {
        BloqueDialogo(
            dialogo = DatosPreview.sala.sala.dialogos.first(),
            onPersonajeClick = {},
            onSalaClick = {},
            modifier = Modifier.padding(Espaciado.l),
        )
    }
}
