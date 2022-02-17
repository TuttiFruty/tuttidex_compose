package fr.tuttifruty.pokeapp.common.di

import fr.tuttifruty.pokeapp.data.repository.PokemonRepositoryImpl
import fr.tuttifruty.pokeapp.data.repository.SpecieRepositoryImpl
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import fr.tuttifruty.pokeapp.domain.repository.SpecieRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PokemonRepository> {
        PokemonRepositoryImpl(
            pokemonDao = get(),
            pokemonService = get(),
        )
    }
    single<SpecieRepository> {
        SpecieRepositoryImpl(
            pokemonDao = get(),
            speciesService = get(),
        )
    }
}