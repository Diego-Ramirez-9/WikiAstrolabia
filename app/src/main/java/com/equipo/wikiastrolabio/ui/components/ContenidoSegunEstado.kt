package com.equipo.wikiastrolabio.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.pixel.EstrellaPixel
import com.equipo.wikiastrolabio.ui.components.pixel.RelojPixel
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/**
 * Pinta carga / error / contenido según el [UiState]. Con [onRecargar], el contenido se puede
 * deslizar hacia abajo para pedir datos nuevos (el contenido debe ser desplazable).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ContenidoSegunEstado(
    uiState: UiState<T>,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    onRecargar: (() -> Unit)? = null,
    contenido: @Composable (T) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            UiState.Cargando -> EstadoCarga()
            is UiState.Error -> EstadoError(error = uiState.error, onReintentar = onReintentar)
            is UiState.Exito -> Column(Modifier.fillMaxSize()) {
                if (uiState.sinConexion) AvisoSinConexion()
                if (onRecargar != null) {
                    PullToRefreshBox(
                        isRefreshing = uiState.recargando,
                        onRefresh = onRecargar,
                        modifier = Modifier.fillMaxSize(),
                    ) { contenido(uiState.datos) }
                } else {
                    contenido(uiState.datos)
                }
            }
        }
    }
}

/** Tres estrellas que se encienden en secuencia mientras se consulta el códex. */
@Composable
fun EstadoCarga(modifier: Modifier = Modifier) {
    val acento = LocalAcento.current
    val transicion = rememberInfiniteTransition(label = "carga")
    val fase by transicion.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1200, easing = LinearEasing), RepeatMode.Restart),
        label = "fase",
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Espaciado.xl)
            .semantics { liveRegion = LiveRegionMode.Polite },
        verticalArrangement = Arrangement.spacedBy(Espaciado.l, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Espaciado.m)) {
            repeat(3) { indice ->
                val encendida = fase.toInt() == indice
                EstrellaPixel(
                    seleccionada = encendida,
                    color = acento,
                    tamano = 20.dp,
                    modifier = Modifier.alpha(if (encendida) 1f else 0.5f),
                )
            }
        }
        Text(
            text = stringResource(R.string.estado_cargando).uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun EstadoError(
    error: ErrorDatos,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Espaciado.xl),
        verticalArrangement = Arrangement.spacedBy(Espaciado.l, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RelojPixel(color = MaterialTheme.colorScheme.error, tamano = 48.dp)
        Text(
            text = stringResource(error.titulo).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(error.mensaje),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        if (error !is ErrorDatos.NoEncontrado) {
            BotonPixel(
                texto = stringResource(R.string.accion_reintentar),
                onClick = onReintentar,
                conFlecha = false,
                modifier = Modifier.padding(top = Espaciado.s),
            )
        }
    }
}

/** Franja superior cuando se muestra contenido guardado porque la API no respondió. */
@Composable
fun AvisoSinConexion(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = Espaciado.margenPantalla, vertical = Espaciado.s),
        horizontalArrangement = Arrangement.spacedBy(Espaciado.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(6.dp)
                .background(MaterialTheme.colorScheme.error),
        )
        Text(
            text = stringResource(R.string.aviso_sin_conexion),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@get:StringRes
private val ErrorDatos.titulo: Int
    get() = when (this) {
        ErrorDatos.SinConexion -> R.string.error_sin_conexion_titulo
        ErrorDatos.NoEncontrado -> R.string.error_no_encontrado_titulo
        else -> R.string.error_generico_titulo
    }

@get:StringRes
private val ErrorDatos.mensaje: Int
    get() = when (this) {
        ErrorDatos.SinConexion -> R.string.error_sin_conexion
        ErrorDatos.NoEncontrado -> R.string.error_no_encontrado
        is ErrorDatos.Servidor -> R.string.error_servidor
        ErrorDatos.DatosInvalidos -> R.string.error_datos_invalidos
        is ErrorDatos.Desconocido -> R.string.error_desconocido
    }

@Preview(showBackground = true, backgroundColor = 0xFF0A131C)
@Composable
private fun EstadosPreview() {
    WikiAstrolabioTheme {
        Column {
            Box(Modifier.size(360.dp, 200.dp)) { EstadoCarga() }
            Box(Modifier.size(360.dp, 320.dp)) { EstadoError(ErrorDatos.SinConexion, onReintentar = {}) }
        }
    }
}
