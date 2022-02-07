package fr.tuttifruty.pokeapp.ui.pokemon.section

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.ui.common.camera.CameraConstantes.Companion.EMPTY_IMAGE_URI
import fr.tuttifruty.pokeapp.ui.theme.PokeAppTheme
import fr.tuttifruty.pokeapp.ui.theme.Purple500
import fr.tuttifruty.pokeapp.ui.theme.Teal700

@Composable
fun BaseStatsSection(pokemon: Pokemon) {
    StatsTable(pokemon.stats)
}

@Composable
private fun StatsTable(stats: List<Pokemon.Stat>) {

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(stats) { stats ->
            var progress by remember { mutableStateOf(0f) }
            val progressAnimDuration = 5500
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(
                    durationMillis = progressAnimDuration,
                    easing = FastOutSlowInEasing
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(0.2f, fill = true),
                    text = stats.label,
                    style = MaterialTheme.typography.body2.copy(
                        color = Teal700
                    )
                )

                Text(
                    modifier = Modifier.weight(0.1f, fill = true),
                    color = Color.Black,
                    text = stats.value.toString(),
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                LinearProgressIndicator(
                    modifier = Modifier.weight(0.7f, fill = true),
                    progress = animatedProgress,
                    color = Purple500
                )


            }
            Spacer(modifier = Modifier.height(10.dp))
            LaunchedEffect(stats.getProgress()) {
                progress = stats.getProgress()
            }
        }
    }

}

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PokemonViewPreview() {
    PokeAppTheme {
        BaseStatsSection(
            pokemon = Pokemon(
                1,
                "Bulvi",
                "",
                "fire, grass",
                123,
                12f,
                12f,
                true,
                EMPTY_IMAGE_URI.toString(),
                EMPTY_IMAGE_URI.toString(),
                listOf(
                    Pokemon.Stat.HP(10,10),
                    Pokemon.Stat.Attack(10,10),
                    Pokemon.Stat.Defense(10,10),
                    Pokemon.Stat.SpAttack(10,10),
                    Pokemon.Stat.SpDefense(10,10),
                    Pokemon.Stat.Speed(10,10),
                    Pokemon.Stat.Total(60,60)
                ),
                "boulou",
            )
        )
    }
}