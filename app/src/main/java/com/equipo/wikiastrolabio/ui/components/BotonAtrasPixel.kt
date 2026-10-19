package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Background

/**
 * Botón atrás pixel-art unificado según el prototipo de Figma:
 * - Recuadro cuadrado de 38x38 dp sobre fondo Background.
 * - Borde dorado ámbar (1.5 dp, Ambar400) con esquinas retro de 2 dp.
 * - Flecha pixel art ic_atras en color ámbar.
 */
@Composable
fun BotonAtrasPixel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .background(Background, RoundedCornerShape(2.dp))
            .border(BorderStroke(1.5.dp, Ambar400), RoundedCornerShape(2.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_atras),
            contentDescription = stringResource(R.string.accion_atras),
            tint = Ambar400,
            modifier = Modifier.size(16.dp),
        )
    }
}
