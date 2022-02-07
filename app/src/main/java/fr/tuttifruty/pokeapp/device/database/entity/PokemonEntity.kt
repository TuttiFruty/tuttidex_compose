package fr.tuttifruty.pokeapp.device.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.ui.common.camera.CameraConstantes.Companion.EMPTY_IMAGE_URI

@Entity(tableName = "pokemon_table")
data class PokemonEntity(
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
    var isCaptured: Boolean = false,

    @ColumnInfo(name = "pokemon_image_of_capture_front")
    var imageOfCaptureFront: String? = null,

    @ColumnInfo(name = "pokemon_image_of_capture_back")
    var imageOfCaptureBack: String? = null,

    @ColumnInfo(name = "pokemon_hp")
    val hp: Int,

    @ColumnInfo(name = "pokemon_attack")
    val attack: Int,

    @ColumnInfo(name = "pokemon_defense")
    val defense: Int,

    @ColumnInfo(name = "pokemon_spAttack")
    val spAttack: Int,

    @ColumnInfo(name = "pokemon_spDefense")
    val spDefense: Int,

    @ColumnInfo(name = "pokemon_speed")
    val speed: Int,

    @ColumnInfo(name = "pokemon_totalStat")
    val totalStat: Int,

    @ColumnInfo(name = "pokemon_species")
    val species: Int? = null,

    @ColumnInfo(name = "pokemon_description")
    val description: String? = null

) {
    fun initFromStat(): List<Pokemon.Stat> {
        val maxStat = maxOf(this.hp, this.attack, this.defense, this.spAttack, this.spDefense, this.speed)
        return listOf(
            Pokemon.Stat.HP(this.hp,maxStat),
            Pokemon.Stat.Attack(this.attack,maxStat),
            Pokemon.Stat.Defense(this.defense,maxStat),
            Pokemon.Stat.SpAttack(this.spAttack,maxStat),
            Pokemon.Stat.SpDefense(this.spDefense,maxStat),
            Pokemon.Stat.Speed(this.speed,maxStat),
            Pokemon.Stat.Total(this.totalStat,this.totalStat),
        )
    }
}

fun PokemonEntity.asDomainModel(): Pokemon {
    return Pokemon(
        number = this.id,
        name = this.name,
        imageUrl = this.image,
        types = this.types,
        baseExp = this.baseExperience,
        height = this.height.toFloat(),
        weight = this.weight.toFloat(),
        isCaptured = this.isCaptured,
        imageOfCaptureFront = this.imageOfCaptureFront,
        imageOfCaptureBack = this.imageOfCaptureBack,
        stats = initFromStat(),
        description = this.description?:"No description available"
    )
}

