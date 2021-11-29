package fr.tuttifruty.pokeapp.common.di

import fr.tuttifruty.pokeapp.data.repository.PokemonRepositoryImpl
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PokemonRepository> { PokemonRepositoryImpl(get(), get()) }
}