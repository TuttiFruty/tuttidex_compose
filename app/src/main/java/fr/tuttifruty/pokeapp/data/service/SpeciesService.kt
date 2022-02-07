package fr.tuttifruty.pokeapp.data.service

import fr.tuttifruty.pokeapp.data.model.SpecieNetwork
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SpeciesService {
    @GET("pokemon-species/{specieId}")
    suspend fun getSpeciesForPokemon(@Path("specieId") specieId: Int): Response<SpecieNetwork>
}