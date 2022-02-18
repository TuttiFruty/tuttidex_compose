package fr.tuttifruty.pokeapp.domain.usecase

import arrow.core.Either
import fr.tuttifruty.pokeapp.domain.UseCase
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import fr.tuttifruty.pokeapp.domain.usecase.PersistPokemonUseCase.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PersistPokemonUseCase :
    UseCase<PokemonToUpdate, Either<PersistPokemonUseCaseErrors, Nothing?>> {

    data class PokemonToUpdate(
        val pokemon: Pokemon
    ) : UseCase.InputValues

    sealed interface PersistPokemonUseCaseErrors : UseCase.Errors
    class FailedToPersistPokemon(override val message: String = "An error occurred during save process") :
        PersistPokemonUseCaseErrors

    class PokemonToPersistMustExist(override val message: String = "No pokemon found to save modification into") :
        PersistPokemonUseCaseErrors
}

class PersistPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : PersistPokemonUseCase {
    override suspend fun invoke(input: PokemonToUpdate?): Either<PersistPokemonUseCaseErrors, Nothing?> =
        withContext(dispatcher) {
            if (input != null) {
                when (pokemonRepository.updatePokemon(input.pokemon)) {
                    null -> Either.Left(FailedToPersistPokemon())
                    else -> Either.Right(null)
                }
            } else {
                Either.Left(PokemonToPersistMustExist())
            }
        }
}
