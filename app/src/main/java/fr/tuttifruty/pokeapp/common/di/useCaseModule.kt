package fr.tuttifruty.pokeapp.common.di

import fr.tuttifruty.pokeapp.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single<PersistAllPokemonUseCase> {
        PersistAllPokemonUseCaseImpl(
            get()
        )
    }

    single<GetAllPokemonUseCase> {
        GetAllPokemonUseCaseImpl(
            get()
        )
    }

    single<PersistPokemonUseCase> {
        PersistPokemonUseCaseImpl(
            get()
        )
    }
}
