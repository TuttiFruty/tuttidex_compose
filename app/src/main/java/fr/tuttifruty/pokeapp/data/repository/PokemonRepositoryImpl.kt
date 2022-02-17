package fr.tuttifruty.pokeapp.data.repository

import android.net.Uri
import arrow.core.Either
import fr.tuttifruty.pokeapp.data.model.asEntity
import fr.tuttifruty.pokeapp.data.service.PokemonService
import fr.tuttifruty.pokeapp.device.database.dao.PokemonDao
import fr.tuttifruty.pokeapp.device.database.entity.PokemonEntity
import fr.tuttifruty.pokeapp.device.database.entity.asDomainModel
import fr.tuttifruty.pokeapp.domain.UseCase
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import fr.tuttifruty.pokeapp.domain.usecase.PersistAllPokemonUseCase
import fr.tuttifruty.pokeapp.ui.common.camera.CameraConstantes.Companion.EMPTY_IMAGE_URI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

class PokemonRepositoryImpl(
    private val pokemonService: PokemonService,
    private val pokemonDao: PokemonDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val emptyUrl: String = EMPTY_IMAGE_URI.toString(),
) : PokemonRepository {

    //TODO SEE IF THEIR IS A BETTER WAY THAN return@withContext
    override suspend fun persistPokemons(): Either<UseCase.Errors, Int> = withContext(dispatcher) {
        pokemonService.getAllPokemon(0, 0)
            .map { countResultNetwork ->
                countResultNetwork.count
                    .takeIf { count -> isUpdatingNeeded(count) }
                    ?.apply {
                        pokemonService.getAllPokemon(this, 0)
                            .map { finalResultNetwork ->
                                finalResultNetwork.results.map { pokemonFromList ->
                                    pokemonFromList.getIdFromUrl()?.let { idPokemon ->
                                        handlePokemonFromNetworkToDB(idPokemon)
                                    }
                                }
                                return@withContext Either.Right(finalResultNetwork.count)
                            }.mapLeft {
                                return@withContext Either.Left(PersistAllPokemonUseCase.FailedToPersistAllPokemons())
                            }
                    }
                return@withContext Either.Left(PersistAllPokemonUseCase.FailedToPersistAllPokemons())
            }.mapLeft {
                return@withContext Either.Left(PersistAllPokemonUseCase.FailedToPersistAllPokemons())
            }
    }

    private suspend fun isUpdatingNeeded(countValue: Int): Boolean {
        //We get none just to retrieve the count value
        val pokemonInDatabase = pokemonDao.getCountOfPokemons()
        return pokemonInDatabase != countValue
    }

    private suspend fun handlePokemonFromNetworkToDB(idPokemon: Int) {
        if (!pokemonDao.hasPokemon(idPokemon)) {
            pokemonService.getPokemon(idPokemon).map {
                pokemonDao.insert(it.asEntity())
            }
        }
    }

    override fun getPokemons(): Flow<List<Pokemon>> {
        return pokemonDao.getAllPokemon()
            .map { it.map { pokemonEntity -> pokemonEntity.asDomainModel() } }
    }

    override fun getPokemon(numberPokemon: Int): Flow<Pokemon?> {
        return pokemonDao.getPokemonAsFlow(numberPokemon).map { it?.asDomainModel() }
    }

    override suspend fun updatePokemon(pokemon: Pokemon): Int {
        return withContext(dispatcher) {
            val pokemonEntity = pokemonDao.getPokemon(pokemon.number)
            if (pokemonEntity != null) {
                pokemonEntity.isCaptured = !pokemonEntity.isCaptured
                tryToDeleteFiles(pokemon, pokemonEntity)
                pokemonEntity.imageOfCaptureBack = pokemon.imageOfCaptureBack
                pokemonEntity.imageOfCaptureFront = pokemon.imageOfCaptureFront
                pokemonDao.update(pokemonEntity)
                1
            } else {
                0
            }
        }
    }

    override suspend fun hasPokemonSpecieInformation(numberPokemon: Int): Boolean {
        return withContext(dispatcher) {
            pokemonDao.getPokemon(numberPokemon)?.description != null
        }
    }

    private fun tryToDeleteFiles(pokemon: Pokemon, pokemonEntity: PokemonEntity) {
        if (pokemon.imageOfCaptureBack == emptyUrl) {
            Uri.parse(pokemonEntity.imageOfCaptureBack).path?.let {
                val fileToDelete = File(it)
                fileToDelete.delete()
            }
        }

        if (pokemon.imageOfCaptureFront == emptyUrl) {
            Uri.parse(pokemonEntity.imageOfCaptureFront).path?.let {
                val fileToDelete = File(it)
                fileToDelete.delete()
            }
        }
    }
}