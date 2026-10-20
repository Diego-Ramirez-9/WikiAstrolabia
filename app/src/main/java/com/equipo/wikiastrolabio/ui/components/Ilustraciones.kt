package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.ui.components.pixel.CampoEstelar
import com.equipo.wikiastrolabio.ui.components.pixel.IconoIlustracion
import com.equipo.wikiastrolabio.ui.components.pixel.esquinasTecnicas
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento

/**
 * Imagen de la API (Supabase Storage). Mientras no hay URL, mientras carga o si falla,
 * muestra [alternativa]. FilterQuality.None mantiene nítido el pixel art al escalarlo.
 */
@Composable
fun ImagenRemota(
    url: String?,
    descripcion: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    alternativa: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        if (url.isNullOrBlank()) {
            alternativa()
            return@Box
        }
        val painter = rememberAsyncImagePainter(model = url, filterQuality = FilterQuality.None)
        val estado by painter.state.collectAsState()
        if (estado !is AsyncImagePainter.State.Success) alternativa()
        Image(
            painter = painter,
            contentDescription = descripcion,
            contentScale = contentScale,
            modifier = Modifier.matchParentSize(),
        )
    }
}

/**
 * Marco de ilustración (personajes, zonas, salas, entradas). Sin imagen todavía, dibuja un
 * cielo estrellado propio de cada elemento ([semilla]) teñido con el acento de la pantalla y
 * la leyenda "Ilustración pendiente". Cuando la API traiga la URL, aparece la imagen real.
 */
@Composable
fun MarcoIlustracion(
    url: String?,
    descripcion: String,
    modifier: Modifier = Modifier,
    semilla: String = descripcion,
    relacionAspecto: Float = 16f / 9f,
    mostrarLeyenda: Boolean = true,
) {
    val acento = LocalAcento.current
    val forma = MaterialTheme.shapes.large
    ImagenRemota(
        url = url,
        descripcion = descripcion,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(relacionAspecto)
            .clip(forma)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, forma)
            .esquinasTecnicas(acento.copy(alpha = 0.55f)),
    ) {
        CampoEstelar(semilla = semilla, acento = acento, modifier = Modifier.fillMaxSize())
        if (mostrarLeyenda) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Espaciado.l),
                verticalArrangement = Arrangement.spacedBy(Espaciado.xs),
            ) {
                IconoIlustracion(tamano = 20.dp)
                Text(
                    text = stringResource(R.string.ilustracion_pendiente).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                )
            }
        }
    }
}

/**
 * Recuadro cuadrado con borde punteado para íconos (habilidades, colecciones). Muestra el
 * ícono de la API si existe; si no, el ícono pixel de [alternativa].
 */
@Composable
fun MarcoIcono(
    url: String?,
    modifier: Modifier = Modifier,
    tamano: Dp = 52.dp,
    alternativa: @Composable () -> Unit,
) {
    val colorBorde = MaterialTheme.colorScheme.outline
    Box(
        modifier = modifier
            .size(tamano)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest, MaterialTheme.shapes.small)
            .drawBehind {
                val trazo = 1.dp.toPx()
                drawRect(
                    color = colorBorde,
                    topLeft = Offset(trazo / 2, trazo / 2),
                    size = Size(size.width - trazo, size.height - trazo),
                    style = Stroke(width = trazo, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3.dp.toPx(), 3.dp.toPx()))),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        ImagenRemota(
            url = url,
            descripcion = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(Espaciado.s),
        ) {
            Box(Modifier.matchParentSize(), contentAlignment = Alignment.Center) { alternativa() }
        }
    }
}
