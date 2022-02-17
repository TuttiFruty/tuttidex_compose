package fr.tuttifruty.pokeapp.domain.repository

import arrow.core.Either
import fr.tuttifruty.pokeapp.domain.UseCase
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun persistPokemons(): Either<UseCase.Errors, Int>
    fun getPokemons(): Flow<List<Pokemon>>
    fun getPokemon(numberPokemon: Int): Flow<Pokemon?>
    suspend fun updatePokemon(pokemon: Pokemon): Int
    suspend fun hasPokemonSpecieInformation(numberPokemon: Int): Boolean
}
