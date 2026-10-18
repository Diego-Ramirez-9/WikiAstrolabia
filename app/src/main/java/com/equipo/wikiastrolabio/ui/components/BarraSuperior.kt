package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.equipo.wikiastrolabio.R

/**
 * Barra superior con título y flecha para regresar (la flecha solo aparece con [onAtras]).
 * Sin insets propios: el Scaffold de AstrolabioApp ya deja el espacio de la barra de estado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    modifier: Modifier = Modifier,
    onAtras: (() -> Unit)? = null,
) {
    TopAppBar(
        title = { Text(titulo, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        modifier = modifier,
        navigationIcon = {
            if (onAtras != null) {
                IconButton(onClick = onAtras) {
                    Icon(
                        painter = painterResource(R.drawable.ic_atras),
                        contentDescription = stringResource(R.string.accion_atras),
                    )
                }
            }
        },
        windowInsets = TopAppBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal),
    )
}
