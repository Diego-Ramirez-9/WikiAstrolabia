// Archivo de compilación raíz: solo declara los plugins; cada módulo los aplica.
plugins {
    alias(libs.plugins.android.application) apply false
    // AGP 9 trae Kotlin integrado (2.2.10). Declarar kotlin-android aquí (sin aplicarlo) fija
    // el compilador a libs.versions.kotlin, que necesitan las librerías actuales (Coil 3, Hilt…).
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}
