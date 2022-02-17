package fr.tuttifruty.pokeapp.common.di

import fr.tuttifruty.pokeapp.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single<PersistAllPokemonUseCase> {
        PersistAllPokemonUseCaseImpl(
            pokemonRepository = get()
        )
    }

    single<GetAllPokemonUseCase> {
        GetAllPokemonUseCaseImpl(
            pokemonRepository = get()
        )
    }

    single<GetPokemonUseCase> {
        GetPokemonUseCaseImpl(
            pokemonRepository = get(),
            specieRepository = get()
        )
    }

    single<PersistPokemonUseCase> {
        PersistPokemonUseCaseImpl(
            pokemonRepository = get()
        )
    }
}
