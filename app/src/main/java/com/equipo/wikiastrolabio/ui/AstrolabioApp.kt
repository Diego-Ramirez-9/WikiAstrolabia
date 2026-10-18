package com.equipo.wikiastrolabio.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.equipo.wikiastrolabio.ui.navigation.AstrolabioNavHost
import com.equipo.wikiastrolabio.ui.navigation.DestinoPrincipal
import com.equipo.wikiastrolabio.ui.navigation.navegarAPestana
import com.equipo.wikiastrolabio.ui.navigation.perteneceA

/** Raíz de la UI: barra de navegación inferior + contenido de la pestaña actual. */
@Composable
fun AstrolabioApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BarraNavegacion(
                destinoActual = entradaActual?.destination,
                onDestinoClick = navController::navegarAPestana,
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

@Composable
private fun BarraNavegacion(
    destinoActual: NavDestination?,
    onDestinoClick: (DestinoPrincipal) -> Unit,
) {
    NavigationBar {
        DestinoPrincipal.entries.forEach { destino ->
            NavigationBarItem(
                selected = destinoActual.perteneceA(destino),
                onClick = { onDestinoClick(destino) },
                icon = { Icon(painterResource(destino.icono), contentDescription = null) },
                label = { Text(stringResource(destino.etiqueta)) },
            )
        }
    }
}
