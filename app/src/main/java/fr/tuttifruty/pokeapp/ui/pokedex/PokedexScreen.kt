package fr.tuttifruty.pokeapp.ui.pokedex

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import fr.tuttifruty.pokeapp.R
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.ui.theme.PokeAppTheme

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun PokedexScreen(viewModel: PokedexViewModel) {

    val onPokemonCaptured: (Pokemon) -> Unit = {
        viewModel.capturePokemon(it)
    }
    val context = LocalContext.current
    when(val uiState = viewModel.uiState.value){
        is PokedexViewModel.UiState.Error -> Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        PokedexViewModel.UiState.Loading -> Loading()
        is PokedexViewModel.UiState.Ready -> Pokedex(pokemons = uiState.pokemons, onPokemonCaptured)
    }
}

@Composable
fun Loading(){
    Box(
        contentAlignment = Alignment.Center, // you apply alignment to all children
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center) // or to a specific child
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Pokedex(
    pokemons: List<Pokemon>,
    onPokemonCaptured: (Pokemon) -> Unit
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LazyVerticalGrid(
                cells = GridCells.Fixed(2)
            ) {
                items(pokemons) { pokemon ->
                    PokemonItem(pokemon = pokemon, onPokemonCaptured)
                }
            }
        }
        else -> {
            LazyColumn {
                items(pokemons) { pokemon ->
                    PokemonItem(pokemon = pokemon, onPokemonCaptured)
                }
            }
        }
    }

}

@ExperimentalMaterialApi
@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onPokemonCaptured: (Pokemon) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /*TODO*/ },
        elevation = Dp(4f),
    ) {
        ConstraintLayout {
            val (imagePokemon, numberPokemon, namePokemon, pokeball) = createRefs()

            Image(
                painter = rememberImagePainter(pokemon.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .constrainAs(imagePokemon) {
                        top.linkTo(parent.top, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                    }
            )

            Text(
                text = stringResource(R.string.number, pokemon.number),
                fontSize = 21.sp,
                modifier = Modifier
                    .constrainAs(numberPokemon) {
                        top.linkTo(imagePokemon.top)
                        bottom.linkTo(imagePokemon.bottom)
                        start.linkTo(imagePokemon.end, margin = 8.dp)
                        width = Dimension.preferredWrapContent
                    }
            )
            Text(
                text = "${pokemon.name}",
                fontSize = 18.sp,
                modifier = Modifier
                    .constrainAs(namePokemon) {
                        baseline.linkTo(numberPokemon.baseline)
                        start.linkTo(numberPokemon.end, margin = 8.dp)
                        end.linkTo(pokeball.start, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    }
            )
            Image(
                painter = painterResource(
                    if (pokemon.isCaptured) {
                        R.drawable.ic_pokeball
                    } else {
                        R.drawable.ic_pokeball_empty
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .constrainAs(pokeball) {
                        end.linkTo(parent.end, margin = 8.dp)
                        top.linkTo(imagePokemon.top)
                        bottom.linkTo(imagePokemon.bottom)
                    }
                    .clip(CircleShape)
                    .clickable {
                        onPokemonCaptured(pokemon)
                    }
            )
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
        Pokedex(
            listOf(
                Pokemon(
                    1,
                    "Bulvi",
                    "",
                    "fire",
                    123,
                    12f,
                    12f,
                    true
                ),
                Pokemon(
                    1,
                    "Bulvi",
                    "",
                    "fire",
                    123,
                    12f,
                    12f,
                    true
                )
            ),
            onPokemonCaptured = {
                Toast.makeText(context, "${it.name}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}