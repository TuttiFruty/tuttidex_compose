package fr.tuttifruty.pokeapp.data.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class ResultNetwork(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NamedResultNetwork>
)
