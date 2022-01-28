package fr.tuttifruty.pokeapp.ui.pokemon

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.usecase.GetAllPokemonUseCase
import fr.tuttifruty.pokeapp.ui.pokemon.PokemonViewModel.UiState.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val pokemonNumber: Int,
    private val getPokemonUseCase: GetAllPokemonUseCase,
) : ViewModel() {

    private val _uiState = mutableStateOf<UiState>(Loading)
    val uiState: State<UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            val result = getPokemonUseCase(GetAllPokemonUseCase.Filters(""))
            if (result.isSuccess) {
                result.getOrNull()?.pokemons
                    ?.flowOn(Dispatchers.Default)
                    ?.collect {
                        _uiState.value =
                            Ready(it.first { pokemon -> pokemon.number == pokemonNumber })
                    }
            } else {
                _uiState.value = Error(result.exceptionOrNull()?.message)
            }

        }
    }

    sealed class UiState {
        object Loading : UiState()
        class Ready(val pokemon: Pokemon) : UiState()
        class Error(val message: String?) : UiState()
    }
}

