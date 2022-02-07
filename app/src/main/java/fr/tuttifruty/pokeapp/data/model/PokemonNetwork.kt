package fr.tuttifruty.pokeapp.data.model

import com.squareup.moshi.Json
import fr.tuttifruty.pokeapp.device.database.entity.PokemonEntity
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class PokemonNetwork(
    val id: Int,
    val name: String,
    val base_experience: Int,
    val height: Int,
    val weight: Int,
    val types: List<TypeNetwork>,
    val sprites: Sprites,
    val stats: List<StatNetwork>,
    val species: NamedResultNetwork,
) {
    fun typesToString(separator: String): String {
        var typesString = ""
        if (types.isNotEmpty()) {
            if (types.size > 1) {
                for (i in 0 until types.size) {
                    typesString = if (i != types.size - 1) {
                        typesString.plus(types[i].type.name.plus(separator))
                    } else {
                        typesString.plus(types[i].type.name)
                    }
                }
            } else {
                typesString = typesString.plus(types[0].type.name)
            }
        }
        return typesString
    }

    fun initFromStats(statName: String? = null): Int {
        return if (statName != null) {
            stats.find { statNetwork -> statNetwork.stat.name == statName }?.base_stat ?: 0
        } else {
            stats.map { it.base_stat }.reduce { acc, baseStat -> acc + baseStat }
        }
    }
}


@JsonSerializable
data class StatNetwork(
    val base_stat: Int,
    val stat: StatTypeNetwork,
)

@JsonSerializable
data class StatTypeNetwork(
    val name: String,
)

@JsonSerializable
data class TypeNetwork(
    val slot: Int,
    val type: NamedResultNetwork
)

@JsonSerializable
data class Sprites(
    val front_default: String?,
    val other: SpritesOther?
)

@JsonSerializable
data class SpritesOther(
    @Json(name = "official-artwork")
    val officialArtwork: SpritesOfficial?,
)

@JsonSerializable
data class SpritesOfficial(
    val front_default: String?,
)

fun PokemonNetwork.asEntity(): PokemonEntity {
    return PokemonEntity(
        id = id,
        name = name,
        baseExperience = base_experience,
        height = height,
        weight = weight,
        types = typesToString(","),
        image = sprites.other?.officialArtwork?.front_default ?: "",
        hp = initFromStats("hp"),
        attack = initFromStats("attack"),
        defense = initFromStats("defense"),
        spAttack = initFromStats("special-attack"),
        spDefense = initFromStats("special-defense"),
        speed = initFromStats("speed"),
        totalStat = initFromStats(),
        species = species.getIdFromUrl()
    )
}