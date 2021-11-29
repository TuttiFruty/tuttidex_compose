package fr.tuttifruty.pokeapp.domain.usecase

import fr.tuttifruty.pokeapp.domain.UseCase
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import fr.tuttifruty.pokeapp.domain.usecase.PersistPokemonUseCase.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PersistPokemonUseCase : UseCase<PokemonToUpdate, Result<Nothing?>> {

    data class PokemonToUpdate(
        val pokemon: Pokemon
    ) : UseCase.InputValues

    sealed class Errors(
        message: String
    ) : Throwable(message = message) {
        class FailedToPersistPokemon :
            Errors(message = "Failed to persist pokemon to data source")
        class PokemonToPersistMustExist :
            Errors(message = "Pokemon to persist must exist")
    }
}


class PersistPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : PersistPokemonUseCase {
    override suspend fun invoke(input: PokemonToUpdate?): Result<Nothing?> {
        return withContext(dispatcher) {
            if(input != null) {
                when (pokemonRepository.updatePokemon(input.pokemon)) {
                    1 -> Result.success(null)
                    else -> Result.failure(Errors.FailedToPersistPokemon())
                }
            }else{
                Result.failure(Errors.PokemonToPersistMustExist())
            }
        }
    }
}
