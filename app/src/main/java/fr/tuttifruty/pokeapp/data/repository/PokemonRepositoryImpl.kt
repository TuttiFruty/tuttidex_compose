package fr.tuttifruty.pokeapp.data.repository

import android.net.Uri
import arrow.core.Either
import arrow.core.flatMap
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

    override suspend fun persistPokemons(): Either<UseCase.Errors, Int> = withContext(dispatcher) {
        pokemonService.getAllPokemon(0, 0)
            .flatMap { resultForCounting ->
                if (isUpdatingNeeded(resultForCounting.count)) {
                    pokemonService.getAllPokemon(resultForCounting.count, 0)
                        .map { resultForParsing ->
                            resultForParsing.results.map { pokemonNamedResources ->
                                pokemonNamedResources.getIdFromUrl()?.let { idPokemon ->
                                    handlePokemonFromNetworkToDB(idPokemon)
                                }
                            }
                            resultForCounting.count
                        }
                        .mapLeft { Either.Left(PersistAllPokemonUseCase.FailedToPersistAllPokemons()) }
                } else {
                    Either.Right(0)
                }
            }
            .mapLeft { PersistAllPokemonUseCase.FailedToPersistAllPokemons() }
    }

    override fun getPokemons(): Flow<List<Pokemon>> =
        pokemonDao.getAllPokemon().map { listPokemon ->
            listPokemon.map { pokemonEntity ->
                pokemonEntity.asDomainModel()
            }
        }


    override fun getPokemon(numberPokemon: Int): Flow<Pokemon?> =
        pokemonDao.getPokemonAsFlow(numberPokemon).map { it?.asDomainModel() }


    override suspend fun updatePokemon(pokemon: Pokemon): Pokemon? = withContext(dispatcher) {
        pokemonDao.getPokemon(pokemon.number)
            ?.run {
                tryToDeleteFiles(pokemon, this)
                pokemonDao.update(
                    this.copy(
                        imageOfCaptureBack = pokemon.imageOfCaptureBack,
                        imageOfCaptureFront = pokemon.imageOfCaptureFront,
                        isCaptured = !this.isCaptured
                    )
                )
            }
            ?.asDomainModel()
    }

    override suspend fun hasPokemonSpecieInformation(numberPokemon: Int): Boolean =
        withContext(dispatcher) {
            pokemonDao.getPokemon(numberPokemon)?.description != null
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

    private suspend fun isUpdatingNeeded(countValue: Int): Boolean =
        pokemonDao.getCountOfPokemons() != countValue

    private suspend fun handlePokemonFromNetworkToDB(idPokemon: Int) {
        if (!pokemonDao.hasPokemon(idPokemon)) {
            pokemonService.getPokemon(idPokemon).map {
                pokemonDao.insert(it.asEntity())
            }
        }
    }
}