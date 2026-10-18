package com.equipo.wikiastrolabio.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/**
 * Pinta cargando / error / contenido según el [UiState]. Todas las pantallas lo usan, así
 * que cambiar el diseño de la carga o del error aquí lo cambia en toda la app.
 *
 * Con [onRecargar], el contenido se puede deslizar hacia abajo para pedir datos nuevos
 * (el contenido debe ser desplazable, ej. un LazyColumn).
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
            UiState.Cargando -> IndicadorCarga()
            is UiState.Error -> MensajeError(error = uiState.error, onReintentar = onReintentar)
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

@Composable
fun IndicadorCarga(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun MensajeError(
    error: ErrorDatos,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Espaciado.l),
        verticalArrangement = Arrangement.spacedBy(Espaciado.m, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(error.mensaje),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        if (error !is ErrorDatos.NoEncontrado) {
            Button(onClick = onReintentar) { Text(stringResource(R.string.accion_reintentar)) }
        }
    }
}

@Composable
fun AvisoSinConexion(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.aviso_sin_conexion),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(Espaciado.s),
    )
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

@Preview(showBackground = true)
@Composable
private fun MensajeErrorPreview() {
    WikiAstrolabioTheme {
        ContenidoSegunEstado(
            uiState = UiState.Error(ErrorDatos.SinConexion),
            onReintentar = {},
        ) {}
    }
}
