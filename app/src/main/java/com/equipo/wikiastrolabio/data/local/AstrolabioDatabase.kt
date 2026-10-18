package com.equipo.wikiastrolabio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.equipo.wikiastrolabio.data.local.cache.CacheRespuestaDao
import com.equipo.wikiastrolabio.data.local.cache.CacheRespuestaEntity

/**
 * Base de datos local. Solo contiene caché que se puede volver a descargar, por eso al
 * cambiar [version] se usa migración destructiva (ver BaseDatosModule) en lugar de escribir
 * migraciones. Si algún día guarda datos del usuario (favoritos, progreso), habrá que
 * exportar el esquema y escribir migraciones reales.
 */
@Database(
    entities = [CacheRespuestaEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AstrolabioDatabase : RoomDatabase() {
    abstract fun cacheRespuestaDao(): CacheRespuestaDao

    companion object {
        const val NOMBRE = "astrolabio.db"
    }
}
