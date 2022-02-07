package fr.tuttifruty.pokeapp.ui.pokemon.section

import androidx.compose.runtime.Composable
import fr.tuttifruty.pokeapp.domain.model.Pokemon

sealed class SectionItem(val title: String, val screen: @Composable () -> Unit) {
    class About(val pokemon: Pokemon) : SectionItem("About", { AboutSection(pokemon) })
    class BaseStats(val pokemon: Pokemon) : SectionItem("Base stats", { BaseStatsSection(pokemon) })
    class Evolution(val pokemon: Pokemon) : SectionItem("Evolution", { EvolutionSection(pokemon) })
    class Moves(val pokemon: Pokemon) : SectionItem("Moves", { MovesSection(pokemon) })
}