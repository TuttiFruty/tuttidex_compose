package fr.tuttifruty.pokeapp.domain.repository

interface SpecieRepository {
    suspend fun tryToPersistSpecieInformationForPokemon(pokemonNumber: Int)
}