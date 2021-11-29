package fr.tuttifruty.pokeapp.domain.usecase

import fr.tuttifruty.pokeapp.domain.UseCase
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import fr.tuttifruty.pokeapp.domain.usecase.PersistAllPokemonUseCase.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext


interface PersistAllPokemonUseCase : UseCase<Nothing?, Result<Nothing?>> {
    sealed class Errors(
        message: String
    ) : Throwable(message = message) {
        class FailedToPersistAllPokemons :
            Errors(message = "Failed to persist all pokemons")
    }
}




class PersistAllPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : PersistAllPokemonUseCase {
    override suspend fun invoke(input: Nothing?): Result<Nothing?> {
        return withContext(dispatcher) {
            val result = pokemonRepository.persistPokemons()
            if (result.isSuccess) {
                Result.success(null)
            } else {
                Result.failure(Errors.FailedToPersistAllPokemons())
            }
        }
    }
}
