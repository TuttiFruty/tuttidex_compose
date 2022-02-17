package fr.tuttifruty.pokeapp.common.di

import fr.tuttifruty.pokeapp.ui.pokedex.PokedexViewModel
import fr.tuttifruty.pokeapp.ui.pokemon.PokemonViewModel
import fr.tuttifruty.pokeapp.ui.pokemoncapture.PokemonCaptureViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // ViewModel
    viewModel {
        PokedexViewModel(
            persistAllPokemonUseCase = get(),
            getAllPokemonUseCase = get(),
            persistPokemonUseCase = get()
        )
    }
    viewModel { (pokemonNumber: Int) ->
        PokemonViewModel(
            pokemonNumber = pokemonNumber,
            getPokemonUseCase = get(),
            persistPokemonUseCase = get(),
        )
    }
    viewModel { (pokemonNumber: Int) ->
        PokemonCaptureViewModel(
            pokemonNumber = pokemonNumber,
            getPokemonUseCase = get(),
            persistPokemonUseCase = get(),
        )
    }
}