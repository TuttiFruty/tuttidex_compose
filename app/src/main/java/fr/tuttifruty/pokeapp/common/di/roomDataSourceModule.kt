package fr.tuttifruty.pokeapp.common.di

import androidx.room.Room
import fr.tuttifruty.pokeapp.device.database.PokemonsDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomDataSourceModule = module {
    // Room Database
    single {
        Room.databaseBuilder(androidApplication(), PokemonsDatabase::class.java, "pokemon_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    // Expose PokemonDAO
    single { get<PokemonsDatabase>().pokemonDao }
}