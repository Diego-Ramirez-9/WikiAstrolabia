package com.equipo.wikiastrolabio

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.equipo.wikiastrolabio.ui.AstrolabioApp
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Crearlo aquí arranca la sincronización del contenido desde el primer momento.
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Debe ir antes de super.onCreate (API de splash de AndroidX).
        installSplashScreen()
        super.onCreate(savedInstanceState)
        mainViewModel // Fuerza su creación (la propiedad es perezosa).

        // La app siempre es oscura: íconos claros en las barras del sistema aunque el
        // teléfono esté en modo claro.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        setContent {
            WikiAstrolabioTheme {
                AstrolabioApp()
            }
        }
    }
}
