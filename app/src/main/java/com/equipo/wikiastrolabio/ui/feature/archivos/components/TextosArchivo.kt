package com.equipo.wikiastrolabio.ui.feature.archivos.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.equipo.wikiastrolabio.R

/**
 * Nombre en singular de la categoría de un archivo ("poemas" → "Poema"). Las categorías
 * nuevas de la API se muestran a partir de su slug ("cartas-de-xi" → "Cartas de xi").
 */
@Composable
fun textoCategoria(slug: String): String = when (slug) {
    "poemas" -> stringResource(R.string.categoria_poema)
    "notas-de-xi" -> stringResource(R.string.categoria_nota_xi)
    else -> slug.replace('-', ' ').replaceFirstChar { it.uppercase() }
}
