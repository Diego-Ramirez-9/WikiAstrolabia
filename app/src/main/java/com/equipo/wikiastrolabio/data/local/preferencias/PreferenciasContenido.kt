package com.equipo.wikiastrolabio.data.local.preferencias

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Valores pequeños que deben sobrevivir al cierre de la app (DataStore). */
@Singleton
class PreferenciasContenido @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    /** Última versión del contenido conocida (GET /contenido/version), o null si nunca se consultó. */
    suspend fun versionContenido(): String? =
        dataStore.data.map { it[VERSION_CONTENIDO] }.first()

    suspend fun guardarVersionContenido(version: String) {
        dataStore.edit { it[VERSION_CONTENIDO] = version }
    }

    private companion object {
        val VERSION_CONTENIDO = stringPreferencesKey("version_contenido")
    }
}
