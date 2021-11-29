package fr.tuttifruty.pokeapp.domain.repository

import fr.tuttifruty.pokeapp.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun persistPokemons() : Result<Int>
    fun getPokemons() : Flow<List<Pokemon>>
    suspend fun updatePokemon(pokemon: Pokemon): Int
}
