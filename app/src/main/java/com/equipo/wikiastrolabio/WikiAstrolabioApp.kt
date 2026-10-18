package com.equipo.wikiastrolabio

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import javax.inject.Inject

/**
 * Punto de entrada de Hilt. También configura Coil para que las imágenes usen el mismo
 * OkHttpClient que la API (un solo pool de conexiones) con su caché en memoria y disco.
 */
@HiltAndroidApp
class WikiAstrolabioApp : Application(), SingletonImageLoader.Factory {

    @Inject
    lateinit var okHttpClient: Lazy<OkHttpClient>

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader.Builder(context)
            .components { add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient.get() })) }
            .build()
}
