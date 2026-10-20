package com.equipo.wikiastrolabio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.equipo.wikiastrolabio.ui.components.Divisor
import com.equipo.wikiastrolabio.ui.navigation.AstrolabioNavHost
import com.equipo.wikiastrolabio.ui.navigation.DestinoPrincipal
import com.equipo.wikiastrolabio.ui.navigation.perteneceA
import com.equipo.wikiastrolabio.ui.navigation.tocarPestana
import com.equipo.wikiastrolabio.ui.theme.Espaciado

/** Raíz de la UI: barra de navegación inferior + contenido de la pestaña actual. */
@Composable
fun AstrolabioApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BarraNavegacion(
                destinoActual = entradaActual?.destination,
                onDestinoClick = navController::tocarPestana,
            )
        },
    ) { padding ->
        AstrolabioNavHost(
            navController = navController,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
        )
    }
}

/**
 * Barra inferior: íconos pixel y etiquetas en versalitas. La pestaña activa se marca con el
 * ámbar y una barra corta encima del ícono (en lugar de la píldora de Material).
 */
@Composable
private fun BarraNavegacion(
    destinoActual: NavDestination?,
    onDestinoClick: (DestinoPrincipal) -> Unit,
) {
    val activo = MaterialTheme.colorScheme.primary
    val inactivo = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    Column {
        Divisor()
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 0.dp,
        ) {
            DestinoPrincipal.entries.forEach { destino ->
                val seleccionado = destinoActual.perteneceA(destino)
                NavigationBarItem(
                    selected = seleccionado,
                    onClick = { onDestinoClick(destino) },
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Espaciado.xs),
                        ) {
                            Box(
                                Modifier
                                    .width(16.dp)
                                    .height(2.dp)
                                    .background(if (seleccionado) activo else Color.Transparent),
                            )
                            Icon(
                                painter = painterResource(destino.icono),
                                contentDescription = null,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(destino.etiqueta).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = activo,
                        selectedTextColor = activo,
                        unselectedIconColor = inactivo,
                        unselectedTextColor = inactivo,
                        indicatorColor = Color.Transparent,
                    ),
                )
            }
        }
    }
}
