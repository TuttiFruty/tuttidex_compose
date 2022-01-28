package fr.tuttifruty.pokeapp

import android.app.Application
import fr.tuttifruty.pokeapp.common.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class PokemonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@PokemonApplication)
            modules(appModule)
        }
    }
}