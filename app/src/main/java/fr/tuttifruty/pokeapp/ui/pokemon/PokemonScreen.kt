package fr.tuttifruty.pokeapp.ui.pokemon

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            ComposeLandscape(pokemon, onClicked, onCaptured)
        }
        else -> {
            ComposePortrait(pokemon, onClicked, onCaptured)
        }
    }
}

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ComposeLandscape(
    pokemon: Pokemon,
    onClicked: (Pokemon) -> Unit,
    onCaptured: (Pokemon) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = pokemon.getColor()
    ) {
        Box(
            modifier = Modifier
                .verticalScroll(enabled = true, state = rememberScrollState())
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-60).dp, y = (-50).dp)
                    .width(150.dp)
                    .height(150.dp)
                    .graphicsLayer {
                        rotationZ = -20f
                    },
                color = Transparent,
                shape = RoundedCornerShape(32.dp)
            ) {}

            PokemonInfoItem(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(start = 300.dp, top = 8.dp)
                    .fillMaxHeight(), pokemon = pokemon,
                contentHeight = 242.dp,
                tabRowHeight = 40.dp,
                tabRowTopTitlePadding = 0.dp
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 50.dp)
                    .size(200.dp)
            ) {
                RotateForEver(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 10.dp), duration = 4000
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

                Image(
                    painter = rememberImagePainter(pokemon.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .align(Alignment.BottomCenter)
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
                        .align(Alignment.CenterEnd)
                        .padding(top = 60.dp)
                        .clip(CircleShape)
                        .clickable {
                            onCaptured(pokemon)
                        }

                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 64.dp, start = 130.dp)

            ) {
                PokemonTypesItem(types = pokemon.getTypesAsList(), fontSize = 16.sp)
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 20.dp, start = 130.dp)
                    .size(32.dp)
                    .clickable {
                        onClicked(pokemon)
                    }
            ) {
                Image(
                    painter = painterResource(
                        if (pokemon.hasNotPicture()) {
                            R.drawable.ic_baseline_add_a_photo_24
                        } else {
                            R.drawable.ic_baseline_photo_camera_24
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
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
private fun ComposePortrait(
    pokemon: Pokemon,
    onClicked: (Pokemon) -> Unit,
    onCaptured: (Pokemon) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = pokemon.getColor()
    ) {
        Box(
            modifier = Modifier
                .verticalScroll(enabled = true, state = rememberScrollState())
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-60).dp, y = (-50).dp)
                    .width(150.dp)
                    .height(150.dp)
                    .graphicsLayer {
                        rotationZ = -20f
                    },
                color = Transparent,
                shape = RoundedCornerShape(32.dp)
            ) {}

            PokemonInfoItem(
                modifier = Modifier
                    .padding(top = 250.dp)
                    .fillMaxSize(), pokemon = pokemon
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp)
                    .size(200.dp)
            ) {
                RotateForEver(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 10.dp), duration = 4000
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

                Image(
                    painter = rememberImagePainter(pokemon.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .align(Alignment.BottomCenter)
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
                        .align(Alignment.CenterEnd)
                        .padding(top = 60.dp)
                        .clip(CircleShape)
                        .clickable {
                            onCaptured(pokemon)
                        }

                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 64.dp, end = 32.dp)

            ) {
                PokemonTypesItem(types = pokemon.getTypesAsList(), fontSize = 16.sp)
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 90.dp, start = 64.dp)
                    .size(32.dp)
                    .clickable {
                        onClicked(pokemon)
                    }
            ) {
                Image(
                    painter = painterResource(
                        if (pokemon.hasNotPicture()) {
                            R.drawable.ic_baseline_add_a_photo_24
                        } else {
                            R.drawable.ic_baseline_photo_camera_24
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                )
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