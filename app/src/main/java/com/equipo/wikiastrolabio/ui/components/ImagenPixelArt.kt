package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.equipo.wikiastrolabio.R

/**
 * Imagen de la API (Supabase Storage) para arte en pixel art.
 *
 * - FilterQuality.None evita que los sprites se vean borrosos al escalarlos.
 * - Si la URL es null (arte aún no subido) o falla la descarga, muestra el marcador
 *   "recurso pendiente" (res/drawable/img_recurso_pendiente.xml), que UI/UX puede reemplazar.
 * - El tamaño lo decide quien la usa mediante [modifier] (ej. Modifier.fillMaxWidth().aspectRatio(16f / 9f)).
 */
@Composable
fun ImagenPixelArt(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val pendiente = painterResource(R.drawable.img_recurso_pendiente)
    if (url == null) {
        Image(
            painter = pendiente,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Inside,
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            modifier = modifier,
            error = pendiente,
            contentScale = contentScale,
            filterQuality = FilterQuality.None,
        )
    }
}
