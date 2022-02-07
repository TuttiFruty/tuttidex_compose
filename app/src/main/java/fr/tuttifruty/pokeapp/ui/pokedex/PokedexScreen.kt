package fr.tuttifruty.pokeapp.ui.pokedex

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.ui.common.Loading
import fr.tuttifruty.pokeapp.ui.common.ParentScreen
import fr.tuttifruty.pokeapp.ui.common.camera.CameraConstantes.Companion.EMPTY_IMAGE_URI
import fr.tuttifruty.pokeapp.ui.theme.PokeAppTheme
import org.koin.androidx.compose.getViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun PokedexScreen(
    viewModel: PokedexViewModel = getViewModel(),
    navController: NavController
) {

    val onCaptured: (Pokemon) -> Unit = {
        viewModel.capturePokemon(it)
    }

    val onClicked: (Pokemon) -> Unit = {
        navController.navigate("pokemon/${it.number}?name=${it.getNameStandardized()}")
    }
    val context = LocalContext.current
    when (val uiState = viewModel.uiState.value) {
        is PokedexViewModel.UiState.Error -> Toast.makeText(
            context,
            uiState.message,
            Toast.LENGTH_SHORT
        ).show()
        PokedexViewModel.UiState.Loading -> Loading()
        is PokedexViewModel.UiState.Ready -> {
            ParentScreen(title = "Pokedex") {
                PokedexView(
                    pokemons = uiState.pokemons,
                    onCaptured = onCaptured,
                    onClicked = onClicked
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun PokedexView(
    pokemons: List<Pokemon>,
    onCaptured: (Pokemon) -> Unit,
    onClicked: (Pokemon) -> Unit,
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(170.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(pokemons) { pokemon ->
            PokemonItem(pokemon = pokemon, onCaptured, onClicked)
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current

    PokeAppTheme {
        PokedexView(
            listOf(
                Pokemon(
                    1,
                    "Bulvi",
                    "",
                    "fire",
                    123,
                    12f,
                    12f,
                    true,
                    EMPTY_IMAGE_URI.toString(),
                    EMPTY_IMAGE_URI.toString(),
                    description = "boulou",
                ),
                Pokemon(
                    1,
                    "Bulvi",
                    "",
                    "fire",
                    123,
                    12f,
                    12f,
                    true,
                    EMPTY_IMAGE_URI.toString(),
                    EMPTY_IMAGE_URI.toString(),
                    description = "boulou",
                    ),
                Pokemon(
                    1,
                    "Bulvi",
                    "",
                    "fire",
                    123,
                    12f,
                    12f,
                    true,
                    EMPTY_IMAGE_URI.toString(),
                    EMPTY_IMAGE_URI.toString(),
                    description = "boulou",
                ),
                Pokemon(
                    1,
                    "Bulvi",
                    "",
                    "fire",
                    123,
                    12f,
                    12f,
                    true,
                    EMPTY_IMAGE_URI.toString(),
                    EMPTY_IMAGE_URI.toString(),
                    description = "boulou",
                )
            ),
            onCaptured = {
                Toast.makeText(context, "Captured ${it.name}", Toast.LENGTH_SHORT).show()
            },
            onClicked = {
                Toast.makeText(context, "Clicked ${it.name}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}