package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.Blanco
import com.equipo.wikiastrolabio.ui.theme.FuentePixelTitulosFallback

/**
 * Barra superior retro con título pixel/serif y botón cuadrado BotonAtrasPixel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    modifier: Modifier = Modifier,
    onAtras: (() -> Unit)? = null,
) {
    TopAppBar(
        title = {
            Text(
                text = titulo.uppercase(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FuentePixelTitulosFallback,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 1.2.sp,
                ),
                color = Blanco,
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (onAtras != null) {
                Box(modifier = Modifier.padding(start = 12.dp, end = 8.dp)) {
                    BotonAtrasPixel(onClick = onAtras)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Background,
        ),
        windowInsets = TopAppBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal),
    )
}
