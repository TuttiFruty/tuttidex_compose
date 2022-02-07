package fr.tuttifruty.pokeapp.ui.pokemoncapture

import android.net.Uri
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.ui.common.ChildScreen
import fr.tuttifruty.pokeapp.ui.common.Loading
import fr.tuttifruty.pokeapp.ui.common.camera.CameraCapture
import fr.tuttifruty.pokeapp.ui.common.camera.CameraConstantes.Companion.EMPTY_IMAGE_URI
import fr.tuttifruty.pokeapp.ui.theme.PokeAppTheme
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun PokemonCaptureScreen(
    pokemonNumber: Int,
    viewModel: PokemonCaptureViewModel = getViewModel(
        parameters = { parametersOf(pokemonNumber) }
    ),
    navController: NavController
) {
    val context = LocalContext.current

    val onCaptured: (Pokemon) -> Unit = {
        viewModel.capturePokemonWithPhoto(it)
    }

    when (val uiState = viewModel.uiState.value) {
        is PokemonCaptureViewModel.UiState.Error -> Toast.makeText(
            context,
            uiState.message,
            Toast.LENGTH_SHORT
        ).show()
        PokemonCaptureViewModel.UiState.Loading -> Loading()
        is PokemonCaptureViewModel.UiState.Ready -> {
            ChildScreen(title = "Photo !", navController = navController) {
                PokemonCaptureView(
                    pokemon = uiState.pokemon,
                    onCaptured = onCaptured,
                )
            }
        }
    }
}

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun PokemonCaptureView(
    pokemon: Pokemon,
    onCaptured: (Pokemon) -> Unit,
) {
    val context = LocalContext.current

    val configuration = context.resources.configuration
    val width: Int = configuration.screenWidthDp
    val height: Int = configuration.screenHeightDp

    val emptyImageFrontCameraUri = EMPTY_IMAGE_URI
    var imageFrontCameraUri by remember { mutableStateOf(Uri.parse(pokemon.imageOfCaptureFront?: EMPTY_IMAGE_URI.toString())) }

    val emptyImageBackCameraUri = EMPTY_IMAGE_URI
    var imageBackCameraUri by remember { mutableStateOf(Uri.parse(pokemon.imageOfCaptureBack?: EMPTY_IMAGE_URI.toString())) }

    if (imageFrontCameraUri != emptyImageFrontCameraUri && imageBackCameraUri != emptyImageBackCameraUri) {
        Box(modifier = Modifier) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberImagePainter(imageFrontCameraUri),
                contentDescription = "Photo of you when you captured the pokemon",
            )

            Image(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        16.dp
                    )
                    .width((width / 3).dp)
                    .height((height / 3).dp),
                painter = rememberImagePainter(imageBackCameraUri),
                contentDescription = "Photo of pokemon captured",
            )
            Button(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = {
                    imageFrontCameraUri = emptyImageFrontCameraUri
                    imageBackCameraUri = emptyImageBackCameraUri
                    onCaptured(
                        pokemon.copy(
                            imageOfCaptureBack = imageBackCameraUri.toString(),
                            imageOfCaptureFront = imageFrontCameraUri.toString(),
                        )
                    )
                },
            ) {
                Text(text = "Release pokemon")
            }
        }
    } else {
        if (imageFrontCameraUri == emptyImageFrontCameraUri) {
            CameraCapture(
                modifier = Modifier,
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
                onImageTaken = {
                    imageFrontCameraUri = it.toUri()
                    onCaptured(
                        pokemon.copy(imageOfCaptureFront = imageFrontCameraUri.toString())
                    )
                }
            )
        } else if (imageBackCameraUri == emptyImageBackCameraUri) {
            CameraCapture(
                modifier = Modifier,
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
                onImageTaken = {
                    imageBackCameraUri = it.toUri()
                    onCaptured(
                        pokemon.copy(imageOfCaptureBack = imageBackCameraUri.toString())
                    )
                }
            )
        }
    }
}

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PokemonCaptureViewPreview() {
    PokeAppTheme {
        PokemonCaptureView(
            pokemon = Pokemon(
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
            onCaptured = {},
        )
    }
}