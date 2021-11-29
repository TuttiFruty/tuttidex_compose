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
    val types: List<Type>,
    val sprites: Sprites
){
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
}

@JsonSerializable
data class Type(
    val slot: Int,
    val type: NamedResultNetwork
)

@JsonSerializable
data class Sprites(
    val front_default : String?
)

fun PokemonNetwork.asEntity(): PokemonEntity {
    return PokemonEntity(
        id = id,
        name = name,
        baseExperience = base_experience,
        height = height,
        weight = weight,
        types = typesToString(","),
        image = sprites.front_default?:""
    )
}