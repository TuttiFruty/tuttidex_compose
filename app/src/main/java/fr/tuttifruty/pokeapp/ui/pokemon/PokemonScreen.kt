package fr.tuttifruty.pokeapp.ui.pokemon

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import fr.tuttifruty.pokeapp.R
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.ui.common.ChildScreen
import fr.tuttifruty.pokeapp.ui.common.Loading
import fr.tuttifruty.pokeapp.ui.common.animation.RotateForEver
import fr.tuttifruty.pokeapp.ui.common.camera.CameraConstantes.Companion.EMPTY_IMAGE_URI
import fr.tuttifruty.pokeapp.ui.pokedex.PokemonTypesItem
import fr.tuttifruty.pokeapp.ui.theme.PokeAppTheme
import fr.tuttifruty.pokeapp.ui.theme.Transparent
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun PokemonScreen(
    pokemonNumber: Int,
    viewModel: PokemonViewModel = getViewModel(
        parameters = { parametersOf(pokemonNumber) }
    ),
    navController: NavHostController
) {
    val onClicked: (Pokemon) -> Unit = {
        navController.navigate("pokemon/${it.number}/photo")
    }

    val onCaptured: (Pokemon) -> Unit = {
        viewModel.capturePokemon(it)
    }

    val context = LocalContext.current
    when (val uiState = viewModel.uiState.value) {
        is PokemonViewModel.UiState.Error -> Toast.makeText(
            context,
            uiState.message,
            Toast.LENGTH_SHORT
        ).show()
        PokemonViewModel.UiState.Loading -> Loading()
        is PokemonViewModel.UiState.Ready -> {
            val numberTitle =
                stringResource(R.string.number, uiState.pokemon.getNumberStandardized())
            ChildScreen(
                title = "$numberTitle ${uiState.pokemon.getNameStandardized()}",
                navController = navController,
                backgroundColor = uiState.pokemon.getColor()
            ) {
                PokemonView(
                    pokemon = uiState.pokemon,
                    onClicked = onClicked,
                    onCaptured = onCaptured,
                )
            }
        }
    }
}

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun PokemonView(
    pokemon: Pokemon,
    onClicked: (Pokemon) -> Unit,
    onCaptured: (Pokemon) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
//            .clickable { onClicked(pokemon) }
            .background(pokemon.getColor())
    ) {
        val (decoration, pokeballDecoration, imagePokemon, pokemonInfo, takePhoto, pokeball, types) = createRefs()

        Surface(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .graphicsLayer {
                    rotationZ = -20f
                }
                .constrainAs(decoration) {
                    top.linkTo(parent.top, margin = -(50.dp))
                    start.linkTo(parent.start, margin = -(60.dp))
                },
            color = Transparent,
            shape = RoundedCornerShape(32.dp)
        ) {}

        RotateForEver(
            modifier = Modifier
                .constrainAs(pokeballDecoration) {
                    bottom.linkTo(imagePokemon.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, duration = 4000
        ) {
            Image(
                modifier = Modifier
                    .size(150.dp)
                    .alpha(0.25f),
                painter = painterResource(id = R.drawable.ic_pokeball_empty),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = null,
            )
        }

        PokemonInfoItem(modifier = Modifier
            .constrainAs(pokemonInfo) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(types.bottom, margin = 8.dp)
            }, pokemon = pokemon
        )

        Image(
            painter = rememberImagePainter(pokemon.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(128.dp)
                .constrainAs(imagePokemon) {
                    top.linkTo(parent.top, margin = 64.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Row(
            modifier = Modifier
                .constrainAs(types) {
                    top.linkTo(imagePokemon.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 32.dp)
                }
        ) {
            PokemonTypesItem(types = pokemon.getTypesAsList(), fontSize = 16.sp)
        }

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
                .offset(x = 10.dp, y = 10.dp)
                .constrainAs(pokeball) {
                    end.linkTo(imagePokemon.end)
                    bottom.linkTo(imagePokemon.bottom)
                }
                .clip(CircleShape)
                .clickable {
                    onCaptured(pokemon)
                }

        )

        Image(
            painter = painterResource(
                R.drawable.ic_baseline_add_a_photo_24
            ),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .constrainAs(takePhoto) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .clickable {
                    onClicked(pokemon)
                }

        )
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
        PokemonView(
            Pokemon(
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
                description = "boulou"
            ),
            onClicked = {},
            onCaptured = {},
        )
    }
}