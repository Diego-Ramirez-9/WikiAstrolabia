package com.equipo.wikiastrolabio.data.local.preferencias

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/** Datos locales de las reseñas (mismo DataStore que PreferenciasContenido). */
@Singleton
class PreferenciasResenas @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    /** Último apodo usado, para no tener que escribirlo cada vez. */
    suspend fun apodo(): String? = dataStore.data.first()[APODO]

    suspend fun guardarApodo(apodo: String) {
        dataStore.edit { it[APODO] = apodo }
    }

    private companion object {
        val APODO = stringPreferencesKey("resenas_apodo")
    }
}
