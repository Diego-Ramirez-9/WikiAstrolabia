package com.equipo.wikiastrolabio.di

import android.content.Context
import com.equipo.wikiastrolabio.BuildConfig
import com.equipo.wikiastrolabio.data.remote.AstrolabioApi
import com.equipo.wikiastrolabio.data.remote.fake.FakeApiInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/** Cliente HTTP exclusivo de la API (con el interceptor falso y el log). Coil usa el cliente base. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ClienteApi

@Module
@InstallIn(SingletonComponent::class)
object RedModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        // Si la API agrega campos nuevos, la app no se rompe.
        ignoreUnknownKeys = true
        // Un null en un campo con valor por defecto usa el valor por defecto.
        coerceInputValues = true
        explicitNulls = false
    }

    /** Cliente base compartido (mismo pool de conexiones para la API y para las imágenes). */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        // Vercel puede tardar unos segundos en "despertar" la función en la primera petición.
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    @ClienteApi
    fun provideClienteApi(
        base: OkHttpClient,
        @ApplicationContext context: Context,
    ): OkHttpClient = base.newBuilder().apply {
        if (BuildConfig.USE_FAKE_API) {
            addInterceptor(FakeApiInterceptor.desdeAssets(context.assets, BuildConfig.API_BASE_URL))
        }
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        }
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(@ClienteApi cliente: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(cliente)
        .addConverterFactory(json.asConverterFactory("application/json; charset=UTF-8".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideAstrolabioApi(retrofit: Retrofit): AstrolabioApi = retrofit.create(AstrolabioApi::class.java)
}
