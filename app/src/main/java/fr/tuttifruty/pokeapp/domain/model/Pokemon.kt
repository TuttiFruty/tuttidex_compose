package fr.tuttifruty.pokeapp.domain.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import fr.tuttifruty.pokeapp.ui.theme.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    var number: Int,
    val name: String?,
    val imageUrl: String?,
    val types: String?,
    val baseExp: Int?,
    val height: Float?,
    val weight: Float?,
    val isCaptured: Boolean,
    val imageOfCaptureFront: String?,
    val imageOfCaptureBack: String?,
    val stats: List<Stat> = listOf(),
    val description: String,
) : Parcelable {
    fun getNameStandardized(): String {
        return name?.replaceFirstChar { it.uppercaseChar() } ?: ""
    }

    fun getNumberStandardized(): String {
        return getNumberStandardized(this.number)
    }

    fun getWeightStandardizedIntoKg(): String {
        return ("${weight?.div(10)} kg")
    }

    fun getHeightStandardizedIntoCm(): String {
        return ("${height?.times(10)} cm")
    }

    fun getTypesAsList(): List<String>? {
        return types?.split(",")
    }

    fun getColor(): Color {
        return when (getTypesAsList()?.elementAtOrNull(0)) {
            "normal" -> normal
            "fighting" -> fighting
            "flying" -> flying
            "poison" -> poison
            "ground" -> ground
            "rock" -> rock
            "bug" -> bug
            "ghost" -> ghost
            "steel" -> steel
            "fire" -> fire
            "water" -> water
            "grass" -> grass
            "electric" -> electric
            "psychic" -> psychic
            "ice" -> ice
            "dragon" -> dragon
            "dark" -> dark
            "fairy" -> fairy
            "shadow" -> shadow
            else -> unknown
        }
    }

    sealed class Stat(
        val label: String,
    ) : Parcelable {
        abstract val value: Int
        abstract val max: Int

        fun getProgress(): Float = if (max > 0) (1f.times(value)).div(max) else 0f

        @Parcelize
        class HP(override val value: Int, override val max: Int) : Stat("HP")

        @Parcelize
        class Attack(override val value: Int, override val max: Int) : Stat("Attack")

        @Parcelize
        class Defense(override val value: Int, override val max: Int) : Stat("Defense")

        @Parcelize
        class SpAttack(override val value: Int, override val max: Int) : Stat("Sp. Atk")

        @Parcelize
        class SpDefense(override val value: Int, override val max: Int) : Stat("Sp. Def")

        @Parcelize
        class Speed(override val value: Int, override val max: Int) : Stat("Speed")

        @Parcelize
        class Total(override val value: Int, override val max: Int) : Stat("Total")
    }

    companion object {
        fun getNumberStandardized(number: Int): String {
            return when (number) {
                in 1..10 -> "00$number"
                in 10..99 -> "0$number"
                else -> "$number"
            }
        }
    }
}