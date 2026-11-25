package com.equipo.wikiastrolabio.data.remote

import com.equipo.wikiastrolabio.data.remote.dto.ArchivoDto
import com.equipo.wikiastrolabio.data.remote.dto.CategoriaArchivoDto
import com.equipo.wikiastrolabio.data.remote.dto.EntradaHistoriaDto
import com.equipo.wikiastrolabio.data.remote.dto.MecanicasDto
import com.equipo.wikiastrolabio.data.remote.dto.NuevaResenaDto
import com.equipo.wikiastrolabio.data.remote.dto.PersonajeDto
import com.equipo.wikiastrolabio.data.remote.dto.PersonajeResumenDto
import com.equipo.wikiastrolabio.data.remote.dto.ResenaDto
import com.equipo.wikiastrolabio.data.remote.dto.RespuestaApi
import com.equipo.wikiastrolabio.data.remote.dto.ResumenResenasDto
import com.equipo.wikiastrolabio.data.remote.dto.SeccionHistoriaDto
import com.equipo.wikiastrolabio.data.remote.dto.VersionContenidoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Endpoints de la API de Astrolabio. El contenido del juego es de solo lectura; la única
 * ruta que escribe es la de publicar una reseña (POST). URL base: BuildConfig.API_BASE_URL (…/api/v1/).
 * Las rutas son relativas (sin "/" inicial) para que respeten el prefijo /api/v1 de la URL base.
 *
 * Los errores HTTP llegan como HttpException; CacheDeContenido los traduce a ErrorDatos.
 */
interface AstrolabioApi {

    @GET("contenido/version")
    suspend fun versionContenido(): RespuestaApi<VersionContenidoDto>

    @GET("historia")
    suspend fun historia(): RespuestaApi<List<SeccionHistoriaDto>>

    @GET("historia/{slug}")
    suspend fun entradaHistoria(@Path("slug") slug: String): RespuestaApi<EntradaHistoriaDto>

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

    // --- Reseñas (no pasan por la caché por versión: cambian a menudo) ---

    @GET("resenas")
    suspend fun resenas(): RespuestaApi<ResumenResenasDto>

    @POST("resenas")
    suspend fun publicarResena(@Body resena: NuevaResenaDto): RespuestaApi<ResenaDto>
}
