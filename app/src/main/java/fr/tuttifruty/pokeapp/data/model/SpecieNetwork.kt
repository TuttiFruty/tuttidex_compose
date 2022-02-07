package fr.tuttifruty.pokeapp.data.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class SpecieNetwork(
    @Json(name = "evolution_chain")
    val evolutionChain: NamedResultNetwork?,
    @Json(name = "flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntries>,
)

@JsonSerializable
data class FlavorTextEntries(
    @Json(name = "flavor_text")
    val flavorText: String?,
    @Json(name = "language")
    val language: NamedResultNetwork?,
    @Json(name = "version")
    val version: NamedResultNetwork?,
)