package com.equipo.wikiastrolabio.data.remote

import com.equipo.wikiastrolabio.data.remote.dto.ArchivoDto
import com.equipo.wikiastrolabio.data.remote.dto.CategoriaArchivoDto
import com.equipo.wikiastrolabio.data.remote.dto.EntradaHistoriaDto
import com.equipo.wikiastrolabio.data.remote.dto.MapaDto
import com.equipo.wikiastrolabio.data.remote.dto.MecanicasDto
import com.equipo.wikiastrolabio.data.remote.dto.PersonajeDto
import com.equipo.wikiastrolabio.data.remote.dto.PersonajeResumenDto
import com.equipo.wikiastrolabio.data.remote.dto.RespuestaApi
import com.equipo.wikiastrolabio.data.remote.dto.SalaDto
import com.equipo.wikiastrolabio.data.remote.dto.SaludDto
import com.equipo.wikiastrolabio.data.remote.dto.SeccionHistoriaDto
import com.equipo.wikiastrolabio.data.remote.dto.VersionContenidoDto
import com.equipo.wikiastrolabio.data.remote.dto.ZonaDto
import com.equipo.wikiastrolabio.data.remote.dto.ZonaResumenDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Endpoints de la API de Astrolabio (solo lectura). URL base: BuildConfig.API_BASE_URL (…/api/v1/).
 * Las rutas son relativas (sin "/" inicial) para que respeten el prefijo /api/v1 de la URL base.
 *
 * Los errores HTTP llegan como HttpException; CacheDeContenido los traduce a ErrorDatos.
 */
interface AstrolabioApi {

    @GET("salud")
    suspend fun salud(): RespuestaApi<SaludDto>

    @GET("contenido/version")
    suspend fun versionContenido(): RespuestaApi<VersionContenidoDto>

    @GET("historia")
    suspend fun historia(): RespuestaApi<List<SeccionHistoriaDto>>

    @GET("historia/{slug}")
    suspend fun entradaHistoria(@Path("slug") slug: String): RespuestaApi<EntradaHistoriaDto>

    @GET("mapa")
    suspend fun mapa(): RespuestaApi<MapaDto>

    /** [tipo] = "eje" | "casa" | "cierre"; null devuelve todas las zonas. */
    @GET("zonas")
    suspend fun zonas(@Query("tipo") tipo: String? = null): RespuestaApi<List<ZonaResumenDto>>

    @GET("zonas/{slug}")
    suspend fun zona(@Path("slug") slug: String): RespuestaApi<ZonaDto>

    @GET("salas/{slug}")
    suspend fun sala(@Path("slug") slug: String): RespuestaApi<SalaDto>

    @GET("personajes")
    suspend fun personajes(): RespuestaApi<List<PersonajeResumenDto>>

    @GET("personajes/{slug}")
    suspend fun personaje(@Path("slug") slug: String): RespuestaApi<PersonajeDto>

    @GET("mecanicas")
    suspend fun mecanicas(): RespuestaApi<MecanicasDto>

    @GET("archivos")
    suspend fun archivos(): RespuestaApi<List<CategoriaArchivoDto>>

    @GET("archivos/{slug}")
    suspend fun archivo(@Path("slug") slug: String): RespuestaApi<ArchivoDto>
}
