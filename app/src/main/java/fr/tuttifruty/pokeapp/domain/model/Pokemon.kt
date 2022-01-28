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
    val isCaptured: Boolean
) : Parcelable {
    fun getNameStandardized(): String {
        return name?.replaceFirstChar { it.uppercaseChar() } ?: ""
    }

    fun getNumberStandardized(): String {
        return getNumberStandardized(this.number)
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