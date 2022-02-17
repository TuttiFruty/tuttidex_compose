package fr.tuttifruty.pokeapp.data.service

import arrow.core.Either
import fr.tuttifruty.pokeapp.data.model.ErrorBody
import fr.tuttifruty.pokeapp.data.model.PokemonNetwork
import fr.tuttifruty.pokeapp.data.model.ResultNetwork
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun getAllPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Either<ErrorBody, ResultNetwork>

    @GET("pokemon/{pokemonId}")
    suspend fun getPokemon(@Path("pokemonId") pokemonId: Int): Either<ErrorBody, PokemonNetwork>
}