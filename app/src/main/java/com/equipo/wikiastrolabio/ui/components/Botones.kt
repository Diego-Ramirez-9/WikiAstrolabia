package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento

/**
 * Botón del juego. [primario] = relleno con el acento (una acción principal por pantalla);
 * secundario = solo borde. Lleva una flecha pixel al final si [conFlecha].
 */
@Composable
fun BotonPixel(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primario: Boolean = true,
    conFlecha: Boolean = true,
) {
    val acento = LocalAcento.current
    val colorTexto = if (primario) MaterialTheme.colorScheme.onPrimary else acento
    Surface(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = Espaciado.objetivoTactil),
        shape = MaterialTheme.shapes.small,
        color = if (primario) acento else acento.copy(alpha = 0.08f),
        border = if (primario) null else BorderStroke(1.dp, acento.copy(alpha = 0.7f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Espaciado.l, vertical = Espaciado.m),
            horizontalArrangement = Arrangement.spacedBy(Espaciado.s, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(texto.uppercase(), style = MaterialTheme.typography.labelLarge, color = colorTexto)
            if (conFlecha) {
                Icon(
                    painter = painterResource(R.drawable.ic_flecha_derecha),
                    contentDescription = null,
                    tint = colorTexto,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

/** Botón cuadrado de regresar con borde de acento (área tocable de 48 dp). */
@Composable
fun BotonAtras(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val acento = LocalAcento.current
    Box(
        modifier = modifier.size(Espaciado.objetivoTactil),
        contentAlignment = Alignment.CenterStart,
    ) {
        Surface(
            onClick = onClick,
            shape = MaterialTheme.shapes.small,
            color = acento.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, acento.copy(alpha = 0.8f)),
            modifier = Modifier.size(40.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_atras),
                    contentDescription = stringResource(R.string.accion_atras),
                    tint = acento,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
