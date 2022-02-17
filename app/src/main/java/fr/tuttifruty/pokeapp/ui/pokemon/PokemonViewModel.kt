package fr.tuttifruty.pokeapp.ui.pokemon

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.usecase.GetPokemonUseCase
import fr.tuttifruty.pokeapp.domain.usecase.PersistPokemonUseCase
import fr.tuttifruty.pokeapp.ui.pokemon.PokemonViewModel.UiState.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val pokemonNumber: Int,
    private val getPokemonUseCase: GetPokemonUseCase,
    private val persistPokemonUseCase: PersistPokemonUseCase,
) : ViewModel() {

    private val _uiState = mutableStateOf<UiState>(Loading)
    val uiState: State<UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            getPokemonUseCase(GetPokemonUseCase.PokemonToRetrieve(pokemonNumber = pokemonNumber))
                .map { result ->
                    result.pokemon
                        .flowOn(Dispatchers.Default)
                        .collect {
                            _uiState.value = Ready(it)
                        }
                }.mapLeft { error ->
                    Error(error.message)
                }
        }
    }

    fun capturePokemon(it: Pokemon) {
        viewModelScope.launch {
            persistPokemonUseCase.invoke(PersistPokemonUseCase.PokemonToUpdate(it))
        }
    }

    sealed class UiState {
        object Loading : UiState()
        class Ready(val pokemon: Pokemon) : UiState()
        class Error(val message: String?) : UiState()
    }
}

