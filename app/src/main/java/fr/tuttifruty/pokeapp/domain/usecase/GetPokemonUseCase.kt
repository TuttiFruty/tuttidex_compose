package fr.tuttifruty.pokeapp.domain.usecase

import arrow.core.Either
import arrow.core.Validated
import fr.tuttifruty.pokeapp.domain.UseCase
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import fr.tuttifruty.pokeapp.domain.repository.SpecieRepository
import fr.tuttifruty.pokeapp.domain.usecase.GetPokemonUseCase.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext

interface GetPokemonUseCase :
    UseCase<PokemonToRetrieve?, Either<GetPokemonUseCaseErrors, Pokemons>> {

    data class PokemonToRetrieve(
        val pokemonNumber: Int
    ) : UseCase.InputValues

    data class Pokemons(
        val pokemon: Flow<Pokemon>
    ) : UseCase.OutputValues

    sealed interface GetPokemonUseCaseErrors : UseCase.Errors
    class PokemonToRetrieveMustBeGiven(override val message: String = "No pokemon passed to be retrieved") :
        GetPokemonUseCaseErrors
}

class GetPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val specieRepository: SpecieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : GetPokemonUseCase {
    override suspend fun invoke(input: PokemonToRetrieve?): Either<GetPokemonUseCaseErrors, Pokemons> =
        withContext(dispatcher) {
            Validated.fromNullable(input) {
                PokemonToRetrieveMustBeGiven()
            }.map { inputNotNull ->
                if (!pokemonRepository.hasPokemonSpecieInformation(inputNotNull.pokemonNumber)) {
                    specieRepository.tryToPersistSpecieInformationForPokemon(inputNotNull.pokemonNumber)
                }
                Pokemons(pokemonRepository.getPokemon(inputNotNull.pokemonNumber).filterNotNull())
            }.toEither()
        }

}