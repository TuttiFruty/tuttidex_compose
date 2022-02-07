package fr.tuttifruty.pokeapp.ui.pokemon.section

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.tuttifruty.pokeapp.domain.model.Pokemon

@Composable
fun EvolutionSection(pokemon: Pokemon) {
    Text(
        color = Color.Black,
        modifier = Modifier
            .fillMaxHeight(),
        text = "TBD"
    )
}