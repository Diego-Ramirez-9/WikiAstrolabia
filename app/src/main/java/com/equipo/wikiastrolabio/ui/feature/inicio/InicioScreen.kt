package com.equipo.wikiastrolabio.ui.feature.inicio

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.ui.components.EtiquetaSeccion
import com.equipo.wikiastrolabio.ui.components.PanelPixel
import com.equipo.wikiastrolabio.ui.components.pixel.CampoEstelar
import com.equipo.wikiastrolabio.ui.components.pixel.IconoArchivos
import com.equipo.wikiastrolabio.ui.components.pixel.IconoHistoria
import com.equipo.wikiastrolabio.ui.components.pixel.IconoMecanicas
import com.equipo.wikiastrolabio.ui.components.pixel.IconoMundo
import com.equipo.wikiastrolabio.ui.components.pixel.IconoPersonajes
import com.equipo.wikiastrolabio.ui.components.pixel.RelojCosmicoDecorativo
import com.equipo.wikiastrolabio.ui.components.pixel.desvanecerAbajo
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/** Secciones del códex que se muestran en Inicio. */
enum class SeccionCodex(@param:StringRes val titulo: Int, @param:StringRes val descripcion: Int) {
    MUNDO(R.string.codex_mundo, R.string.codex_mundo_desc),
    HISTORIA(R.string.codex_historia, R.string.codex_historia_desc),
    PERSONAJES(R.string.codex_personajes, R.string.codex_personajes_desc),
    MECANICAS(R.string.codex_mecanicas, R.string.codex_mecanicas_desc),
    ARCHIVOS(R.string.codex_archivos, R.string.codex_archivos_desc),
}

/**
 * Portada: no consume la API. Mundo va destacado (es la puerta al contenido principal) y las
 * demás secciones forman una cuadrícula de 2×2.
 */
@Composable
fun InicioScreen(
    onSeccionClick: (SeccionCodex) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Espaciado.margenPantalla,
            end = Espaciado.margenPantalla,
            bottom = Espaciado.xxxl,
        ),
        verticalArrangement = Arrangement.spacedBy(Espaciado.m),
    ) {
        item { Portada() }
        item { EtiquetaSeccion(stringResource(R.string.inicio_explora)) }
        item {
            TarjetaDestacada(
                seccion = SeccionCodex.MUNDO,
                onClick = { onSeccionClick(SeccionCodex.MUNDO) },
            )
        }
        val resto = SeccionCodex.entries.drop(1).chunked(2)
        resto.forEach { par ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(Espaciado.m),
                ) {
                    par.forEach { seccion ->
                        TarjetaCodex(
                            seccion = seccion,
                            onClick = { onSeccionClick(seccion) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Portada() {
    val acento = LocalAcento.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Espaciado.l, bottom = Espaciado.s),
    ) {
        CampoEstelar(
            semilla = "portada",
            acento = acento,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.6f)
                .desvanecerAbajo(),
        )
        RelojCosmicoDecorativo(
            tamano = 200.dp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = Espaciado.s),
        )
        Column(
            modifier = Modifier.padding(top = 112.dp, bottom = Espaciado.l),
            verticalArrangement = Arrangement.spacedBy(Espaciado.s),
        ) {
            Text(
                text = stringResource(R.string.inicio_registro).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = acento,
            )
            Text(
                text = stringResource(R.string.inicio_titulo).uppercase(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.semantics { heading() },
            )
            Spacer(Modifier.height(Espaciado.xs))
            Text(
                text = stringResource(R.string.inicio_lema),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary,
            )
            Text(
                text = stringResource(R.string.inicio_subtitulo),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun TarjetaDestacada(seccion: SeccionCodex, onClick: () -> Unit) {
    val acento = LocalAcento.current
    PanelPixel(onClick = onClick, resaltado = true, relleno = PaddingValues(Espaciado.l)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Espaciado.l),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconoSeccion(seccion, tamano = 48.dp)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Espaciado.xs)) {
                Text(
                    text = stringResource(R.string.inicio_destacado).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = acento,
                )
                Text(
                    text = stringResource(seccion.titulo).uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(seccion.descripcion),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_flecha_derecha),
                contentDescription = null,
                tint = acento,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun TarjetaCodex(seccion: SeccionCodex, onClick: () -> Unit, modifier: Modifier = Modifier) {
    PanelPixel(modifier = modifier, onClick = onClick, separacion = Espaciado.s) {
        IconoSeccion(seccion, tamano = 32.dp)
        Spacer(Modifier.height(Espaciado.xs))
        Text(
            text = stringResource(seccion.titulo).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = stringResource(seccion.descripcion),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun IconoSeccion(seccion: SeccionCodex, tamano: Dp) = when (seccion) {
    SeccionCodex.MUNDO -> IconoMundo(tamano = tamano)
    SeccionCodex.HISTORIA -> IconoHistoria(tamano = tamano)
    SeccionCodex.PERSONAJES -> IconoPersonajes(tamano = tamano)
    SeccionCodex.MECANICAS -> IconoMecanicas(tamano = tamano)
    SeccionCodex.ARCHIVOS -> IconoArchivos(tamano = tamano)
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 900)
@Composable
private fun InicioScreenPreview() {
    WikiAstrolabioTheme {
        InicioScreen(onSeccionClick = {})
    }
}
