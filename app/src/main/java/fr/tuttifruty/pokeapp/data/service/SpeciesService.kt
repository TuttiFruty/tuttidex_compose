package fr.tuttifruty.pokeapp.data.service

import arrow.core.Either
import fr.tuttifruty.pokeapp.data.model.ErrorBody
import fr.tuttifruty.pokeapp.data.model.SpecieNetwork
import retrofit2.http.GET
import retrofit2.http.Path

interface SpeciesService {
    @GET("pokemon-species/{specieId}")
    suspend fun getSpeciesForPokemon(@Path("specieId") specieId: Int): Either<ErrorBody, SpecieNetwork>
}