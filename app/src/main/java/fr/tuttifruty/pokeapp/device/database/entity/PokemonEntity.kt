package fr.tuttifruty.pokeapp.device.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.tuttifruty.pokeapp.domain.model.Pokemon

@Entity(tableName = "pokemon_table")
data class PokemonEntity constructor(
    @PrimaryKey
    var id: Int,

    @ColumnInfo(name = "pokemon_name")
    var name: String,

    @ColumnInfo(name = "pokemon_base_exp")
    var baseExperience: Int,

    @ColumnInfo(name = "pokemon_height")
    var height: Int,

    @ColumnInfo(name = "pokemon_weight")
    var weight: Int,

    @ColumnInfo(name = "pokemon_types")
    var types: String,

    @ColumnInfo(name = "pokemon_image_path")
    var image: String,

    @ColumnInfo(name = "pokemon_is_captured")
    var isCaptured: Boolean = false
)

fun PokemonEntity.asDomainModel() : Pokemon {
    return Pokemon(
        number = this.id,
         name = this.name,
        imageUrl = this.image,
        types = this.types,
        baseExp = this.baseExperience,
        height = this.height.toFloat(),
        weight = this.weight.toFloat(),
        isCaptured = this.isCaptured
    )
}

