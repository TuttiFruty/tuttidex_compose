package fr.tuttifruty.pokeapp.common.di

import fr.tuttifruty.pokeapp.ui.pokedex.PokedexViewModel
import fr.tuttifruty.pokeapp.ui.pokemon.PokemonViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // ViewModel
    viewModel { PokedexViewModel(get(), get(), get()) }
    viewModel { (pokemonNumber: Int) -> PokemonViewModel(pokemonNumber = pokemonNumber,getPokemonUseCase = get()) }
}