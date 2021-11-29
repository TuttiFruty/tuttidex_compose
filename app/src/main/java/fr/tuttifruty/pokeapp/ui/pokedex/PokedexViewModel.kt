package fr.tuttifruty.pokeapp.ui.pokedex

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.usecase.GetAllPokemonUseCase
import fr.tuttifruty.pokeapp.domain.usecase.PersistAllPokemonUseCase
import fr.tuttifruty.pokeapp.domain.usecase.PersistPokemonUseCase
import fr.tuttifruty.pokeapp.domain.usecase.PersistPokemonUseCase.PokemonToUpdate
import fr.tuttifruty.pokeapp.ui.pokedex.PokedexViewModel.UiState.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PokedexViewModel(
    private val persistAllPokemonUseCase: PersistAllPokemonUseCase,
    private val getAllPokemonUseCase: GetAllPokemonUseCase,
    private val persistPokemonUseCase: PersistPokemonUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf<UiState>(Loading)
    val uiState: State<UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            persistAllPokemonUseCase.invoke()
        }
        viewModelScope.launch {
            val result = getAllPokemonUseCase.invoke(GetAllPokemonUseCase.Filters(""))
            if (result.isSuccess) {
                result.getOrNull()?.pokemons
                    ?.flowOn(Dispatchers.Default)
                    ?.collect {
                        _uiState.value = Ready(it)
                    }
            } else {
                _uiState.value = Error(result.exceptionOrNull()?.message)
            }

        }
    }

    fun capturePokemon(it: Pokemon) {
        viewModelScope.launch {
            persistPokemonUseCase.invoke(PokemonToUpdate(it))
        }
    }

    sealed class UiState {
        object Loading : UiState()
        class Ready(val pokemons: List<Pokemon>) : UiState()
        class Error(val message: String?) : UiState()
    }
}

