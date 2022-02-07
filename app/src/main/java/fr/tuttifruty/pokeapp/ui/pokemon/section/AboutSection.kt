package fr.tuttifruty.pokeapp.ui.pokemon.section

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.ui.common.camera.CameraConstantes.Companion.EMPTY_IMAGE_URI
import fr.tuttifruty.pokeapp.ui.theme.PokeAppTheme
import fr.tuttifruty.pokeapp.ui.theme.Teal700

@Composable
fun AboutSection(pokemon: Pokemon) {
    Surface(
        color = Color.White,
        contentColor = Color.Black
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = pokemon.description,
            )

            Card(
                backgroundColor = Color.White,
                contentColor = Color.Black,
                elevation = 4.dp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(108.dp)
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(.5f, true)
                            .padding(start = 32.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(text = "Weight", color = Teal700)
                        Text(text = pokemon.getWeightStandardizedIntoKg())
                    }
                    Column(
                        modifier = Modifier
                            .weight(.5f, true)
                            .padding(start = 16.dp, end = 32.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(text = "Height", color = Teal700)
                        Text(text = pokemon.getHeightStandardizedIntoCm())
                    }
                }
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
fun AboutSectionPreview() {
    PokeAppTheme {
        AboutSection(
            pokemon = Pokemon(
                1,
                "Bulvi",
                "",
                "fire, grass",
                123,
                3f,
                12f,
                true,
                EMPTY_IMAGE_URI.toString(),
                EMPTY_IMAGE_URI.toString(),
                description = "boulou this a very long description of a pokemon so that we can see what happen on screen"
            ),
        )
    }
}