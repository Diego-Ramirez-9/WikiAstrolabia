package com.equipo.wikiastrolabio.data.remote

import com.equipo.wikiastrolabio.data.remote.dto.RespuestaErrorApi
import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

/** Traduce cualquier excepción de Retrofit, OkHttp o kotlinx.serialization a un [ErrorDatos]. */
internal fun Throwable.aErrorDatos(json: Json): ErrorDatos = when (this) {
    is HttpException -> when (code()) {
        404 -> ErrorDatos.NoEncontrado
        in 500..599 -> ErrorDatos.Servidor(code(), mensajeDelServidor(json))
        else -> ErrorDatos.Desconocido(this)
    }
    // Sin internet, DNS, tiempo de espera, conexión cortada…
    is IOException -> ErrorDatos.SinConexion
    is SerializationException -> ErrorDatos.DatosInvalidos
    else -> ErrorDatos.Desconocido(this)
}

/** Lee el campo "mensaje" de `{ "error": { ... } }`, si el cuerpo lo trae. */
private fun HttpException.mensajeDelServidor(json: Json): String? = runCatching {
    val cuerpo = response()?.errorBody()?.string() ?: return null
    json.decodeFromString<RespuestaErrorApi>(cuerpo).error.mensaje
}.getOrNull()
