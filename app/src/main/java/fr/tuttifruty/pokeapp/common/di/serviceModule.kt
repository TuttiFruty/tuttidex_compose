package fr.tuttifruty.pokeapp.common.di

import android.os.Build
import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import com.squareup.moshi.Moshi
import fr.tuttifruty.pokeapp.BuildConfig
import fr.tuttifruty.pokeapp.data.model.KotshiApplicationJsonAdapterFactory
import fr.tuttifruty.pokeapp.data.service.PokemonService
import fr.tuttifruty.pokeapp.data.service.SpeciesService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

val serviceModule = module {
    single<PokemonService> { createWebService(BuildConfig.URL, provideClient()) }
    single<SpeciesService> { createWebService(BuildConfig.URL, provideClient()) }
}

fun provideClient(): OkHttpClient {
    return provideClientBuilder().build()
}

fun provideClientBuilder(withLoggingLevel: Boolean = true): OkHttpClient.Builder {
    val okHttpClientBuilder = OkHttpClient.Builder()

    if (withLoggingLevel) {
        okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
        })
    }

    okHttpClientBuilder
        .addInterceptor(provideHttpInterceptor())

    return okHttpClientBuilder

}

fun provideRetrofitBuilder(url: String): Retrofit.Builder {
    val converter = provideConverter()
    return Retrofit.Builder()
        .baseUrl(url)
        .addCallAdapterFactory(EitherCallAdapterFactory.create())
        .addConverterFactory(converter)
}

fun provideHttpInterceptor(): Interceptor = Interceptor {
    if (!it.request().isHttps && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        throw IOException("Cleartext HTTP traffic to * not permitted")
    } else {
        it.proceed(it.request())
    }
}

fun provideConverter(): Converter.Factory {
    return MoshiConverterFactory.create(
        Moshi.Builder()
            .add(KotshiApplicationJsonAdapterFactory)
            .build()
    )
}

inline fun <reified T> createWebService(url: String, client: OkHttpClient): T {
    val builder = provideRetrofitBuilder(url)

    return builder
        .client(client)
        .build()
        .create(T::class.java)
}