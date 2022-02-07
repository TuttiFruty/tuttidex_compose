package fr.tuttifruty.pokeapp.ui.pokemoncapture

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.usecase.GetAllPokemonUseCase
import fr.tuttifruty.pokeapp.domain.usecase.PersistPokemonUseCase
import fr.tuttifruty.pokeapp.ui.pokemoncapture.PokemonCaptureViewModel.UiState.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PokemonCaptureViewModel(
    private val pokemonNumber: Int,
    private val persistPokemonUseCase: PersistPokemonUseCase,
    private val getAllPokemonUseCase: GetAllPokemonUseCase,
) : ViewModel() {

    private val _uiState = mutableStateOf<UiState>(Loading)
    val uiState: State<UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            val result = getAllPokemonUseCase(GetAllPokemonUseCase.Filters(""))
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

    fun capturePokemonWithPhoto(it: Pokemon) {
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

