package fr.tuttifruty.pokeapp.data.repository

import fr.tuttifruty.pokeapp.data.service.SpeciesService
import fr.tuttifruty.pokeapp.device.database.dao.PokemonDao
import fr.tuttifruty.pokeapp.domain.repository.SpecieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpecieRepositoryImpl(
    private val speciesService: SpeciesService,
    private val pokemonDao: PokemonDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : SpecieRepository {
    override suspend fun tryToPersistSpecieInformationForPokemon(pokemonNumber: Int) {
        withContext(dispatcher) {
            pokemonDao.getPokemon(pokemonNumber)?.apply {
                this.species?.let { speciesId ->
                    speciesService.getSpeciesForPokemon(speciesId).map { specieData ->
                        pokemonDao.update(
                            this.copy(
                                description = specieData.flavorTextEntries
                                    .firstOrNull { it.language?.name == "en" && it.version?.name == "black" }
                                    ?.flavorText
                                    ?: "No description available"
                                        .replace("\\n".toRegex(), " ")
                            ))
                    }
                }
            }
        }
    }
}