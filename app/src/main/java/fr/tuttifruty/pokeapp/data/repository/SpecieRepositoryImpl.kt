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
        withContext(dispatcher){
            pokemonDao.getPokemon(pokemonNumber)?.run {
                if(this.species != null) {
                    val responseSpecieNetwork = speciesService.getSpeciesForPokemon(this.species)
                    if(responseSpecieNetwork.isSuccessful){
                        val resultSpecieNetwork = responseSpecieNetwork.body()
                        val description =
                            resultSpecieNetwork?.flavorTextEntries?.firstOrNull { it.language?.name == "en" && it.version?.name == "black" }?.flavorText
                            ?:"No description available"

                        val reworkerDescription = description.replace("\\n".toRegex(), " ")
                        val pokemonUpdated = this.copy(description = reworkerDescription)
                        pokemonDao.update(pokemonUpdated)
                    }
                }
            }
        }
    }
}