package com.equipo.wikiastrolabio.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.equipo.wikiastrolabio.ui.navigation.AstrolabioNavHost
import com.equipo.wikiastrolabio.ui.navigation.DestinoPrincipal
import com.equipo.wikiastrolabio.ui.navigation.navegarAPestana
import com.equipo.wikiastrolabio.ui.navigation.perteneceA
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.PixelifySans
import com.equipo.wikiastrolabio.ui.theme.Slate700
import com.equipo.wikiastrolabio.ui.theme.Slate800

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
    Column {
        HorizontalDivider(
            thickness = 1.dp,
            color = Slate800,
        )
        NavigationBar(
            containerColor = Background,
            contentColor = Ambar400,
            tonalElevation = 0.dp,
        ) {
            DestinoPrincipal.entries.forEach { destino ->
                val seleccionado = destinoActual.perteneceA(destino)
                NavigationBarItem(
                    selected = seleccionado,
                    onClick = { onDestinoClick(destino) },
                    icon = {
                        Icon(
                            painter = painterResource(destino.icono),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(destino.etiqueta).uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = PixelifySans,
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                                letterSpacing = 1.2.sp,
                                fontSize = 10.sp,
                            ),
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Ambar400,
                        selectedTextColor = Ambar400,
                        unselectedIconColor = Slate700,
                        unselectedTextColor = Slate700,
                        indicatorColor = Color.Transparent,
                    ),
                )
            }
        }
    }
}
