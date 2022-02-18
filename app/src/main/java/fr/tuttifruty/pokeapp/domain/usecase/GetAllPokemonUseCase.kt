package fr.tuttifruty.pokeapp.domain.usecase

import arrow.core.Either
import fr.tuttifruty.pokeapp.domain.UseCase
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import fr.tuttifruty.pokeapp.domain.usecase.GetAllPokemonUseCase.Filters
import fr.tuttifruty.pokeapp.domain.usecase.GetAllPokemonUseCase.Pokemons
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface GetAllPokemonUseCase : UseCase<Filters, Either<Nothing, Pokemons>> {
    data class Filters(
        val filter: String
    ) : UseCase.InputValues

    data class Pokemons(
        val pokemons: Flow<List<Pokemon>>
    ) : UseCase.OutputValues
}

class GetAllPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : GetAllPokemonUseCase {
    override suspend fun invoke(input: Filters?): Either<Nothing, Pokemons> =
        withContext(dispatcher) {
            Either.Right(Pokemons(pokemonRepository.getPokemons()))
        }
}