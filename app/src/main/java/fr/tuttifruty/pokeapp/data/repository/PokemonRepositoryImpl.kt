package fr.tuttifruty.pokeapp.data.repository

import fr.tuttifruty.pokeapp.data.model.asEntity
import fr.tuttifruty.pokeapp.data.service.PokemonService
import fr.tuttifruty.pokeapp.device.database.dao.PokemonDao
import fr.tuttifruty.pokeapp.device.database.entity.asDomainModel
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import fr.tuttifruty.pokeapp.domain.usecase.PersistAllPokemonUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PokemonRepositoryImpl(
    private val pokemonService: PokemonService,
    private val pokemonDao: PokemonDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokemonRepository {
    override suspend fun persistPokemons(): Result<Int> {
        return withContext(dispatcher) {
            val resultForCountValue = pokemonService.getAllPokemon(0, 0)
            val countValue = resultForCountValue.body()?.count
            if (countValue != null && isUpdatingNeeded(countValue)) {
                //Now we retrieve all the pokemon available
                val resultForAllPokemon = pokemonService.getAllPokemon(countValue, 0)
                val listOfPokemon = resultForAllPokemon.body()?.results
                if (resultForAllPokemon.isSuccessful && listOfPokemon != null && listOfPokemon.isNotEmpty()) {
                    listOfPokemon.map { networkResult ->
                        networkResult.getIdFromUrl()?.let { idPokemon ->
                            handlePokemonFromNetworkToDB(idPokemon)
                        }
                    }
                    Result.success(countValue)
                } else {
                    Result.failure(PersistAllPokemonUseCase.Errors.FailedToPersistAllPokemons())
                }
            } else {
                Result.failure(PersistAllPokemonUseCase.Errors.FailedToPersistAllPokemons())
            }
        }
    }

    private suspend fun isUpdatingNeeded(countValue: Int): Boolean {
        //We get none just to retrieve the count value
        val pokemonInDatabase = pokemonDao.getCountOfPokemons()
        return pokemonInDatabase != countValue
    }

    private suspend fun handlePokemonFromNetworkToDB(idPokemon: Int) {
        if(!pokemonDao.hasPokemon(idPokemon)) {
            val resultPokemonFromNetwork = pokemonService.getPokemon(idPokemon)
            val pokemonFromNetwork = resultPokemonFromNetwork.body()
            if (resultPokemonFromNetwork.isSuccessful && pokemonFromNetwork != null) {
                pokemonDao.insert(pokemonFromNetwork.asEntity())
            }
        }
    }

    override fun getPokemons(): Flow<List<Pokemon>> {
        return pokemonDao.getAllPokemon()
            .map { it.map { pokemonEntity -> pokemonEntity.asDomainModel() } }
    }

    override suspend fun updatePokemon(pokemon: Pokemon): Int {
        return withContext(dispatcher) {
            val pokemonEntity = pokemonDao.getPokemon(pokemon.number)
            if (pokemonEntity != null) {
                pokemonEntity.isCaptured = !pokemonEntity.isCaptured
                pokemonDao.update(pokemonEntity)
                1
            } else {
                0
            }
        }
    }
}