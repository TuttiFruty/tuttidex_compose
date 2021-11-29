package fr.tuttifruty.pokeapp.common.di

import fr.tuttifruty.pokeapp.ui.pokedex.PokedexViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // ViewModel
    viewModel { PokedexViewModel(get(), get(), get()) }
}