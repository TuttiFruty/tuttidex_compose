package fr.tuttifruty.pokeapp.domain.usecase

import fr.tuttifruty.pokeapp.domain.UseCase
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import fr.tuttifruty.pokeapp.domain.repository.SpecieRepository
import fr.tuttifruty.pokeapp.domain.usecase.GetPokemonUseCase.*
import fr.tuttifruty.pokeapp.domain.usecase.GetPokemonUseCase.Errors.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext

interface GetPokemonUseCase : UseCase<PokemonToRetrieve?, Result<Pokemons>> {

    data class PokemonToRetrieve(
        val pokemonNumber: Int
    ) : UseCase.InputValues

    data class Pokemons(
        val pokemon: Flow<Pokemon>
    ) : UseCase.OutputValues

    sealed class Errors(
        message: String
    ) : Throwable(message = message) {
        class PokemonToRetrieveMustBeGiven :
            Errors(message = "Pokemon to retrieve must be given")
    }
}

class GetPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val specieRepository: SpecieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : GetPokemonUseCase {
    override suspend fun invoke(input: PokemonToRetrieve?): Result<Pokemons> {
        return withContext(dispatcher) {
            if(input != null){
                if(!pokemonRepository.hasPokemonSpecieInformation(input.pokemonNumber)){
                    specieRepository.tryToPersistSpecieInformationForPokemon(input.pokemonNumber)
                }
                Result.success(Pokemons(pokemonRepository.getPokemon(input.pokemonNumber).filterNotNull()))
            }else{
                Result.failure(PokemonToRetrieveMustBeGiven())
            }
        }
    }

}