package fr.tuttifruty.pokeapp.domain.usecase

import arrow.core.Either
import fr.tuttifruty.pokeapp.domain.UseCase
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import fr.tuttifruty.pokeapp.domain.usecase.PersistAllPokemonUseCase.PersistAllPokemonUseCaseErrors
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


interface PersistAllPokemonUseCase :
    UseCase<Nothing?, Either<PersistAllPokemonUseCaseErrors, Nothing?>> {

    sealed interface PersistAllPokemonUseCaseErrors : UseCase.Errors
    class FailedToPersistAllPokemons(override val message: String = "An error occurred during save process") :
        PersistAllPokemonUseCaseErrors
}

class PersistAllPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : PersistAllPokemonUseCase {
    override suspend fun invoke(input: Nothing?): Either<PersistAllPokemonUseCaseErrors, Nothing?> =
        withContext(dispatcher) {
            pokemonRepository.persistPokemons()
                .map { null }
                .mapLeft { errors -> errors as PersistAllPokemonUseCaseErrors }
        }
}
