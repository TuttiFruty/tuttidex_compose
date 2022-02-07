package fr.tuttifruty.pokeapp.ui.pokedex

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.layout.ContentScale
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
import fr.tuttifruty.pokeapp.ui.common.camera.CameraConstantes.Companion.EMPTY_IMAGE_URI
import fr.tuttifruty.pokeapp.ui.theme.PokeAppTheme


@ExperimentalMaterialApi
@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onCaptured: (Pokemon) -> Unit,
    onClicked: (Pokemon) -> Unit
) {
    Card(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth(),
        onClick = { onClicked(pokemon) },
        elevation = Dp(4f),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = pokemon.getColor()
    ) {
        ConstraintLayout {
            val (imagePokemon, numberPokemon, namePokemon, pokeball, types, pokeballBg) = createRefs()

            Text(
                text = pokemon.getNameStandardized(),
                fontSize = 16.sp,
                color = Black,
                modifier = Modifier
                    .constrainAs(namePokemon) {
                        top.linkTo(parent.top, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(numberPokemon.start)
                        width = Dimension.fillToConstraints
                    }
            )

            Text(
                text = stringResource(R.string.number, pokemon.getNumberStandardized()),
                fontSize = 14.sp,
                color = Black,
                modifier = Modifier
                    .constrainAs(numberPokemon) {
                        baseline.linkTo(namePokemon.baseline)
                        start.linkTo(namePokemon.end, margin = 4.dp)
                        end.linkTo(pokeball.start)
                        width = Dimension.preferredWrapContent
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
                    .size(24.dp)
                    .constrainAs(pokeball) {
                        start.linkTo(numberPokemon.end, margin = 4.dp)
                        end.linkTo(parent.end, margin = 4.dp)
                        top.linkTo(numberPokemon.top)
                        bottom.linkTo(numberPokemon.bottom)
                    }
                    .clip(CircleShape)
                    .clickable {
                        onCaptured(pokemon)
                    }
            )

            Column(
                modifier = Modifier
                    .constrainAs(types) {
                        top.linkTo(namePokemon.bottom, margin = 8.dp)
                        start.linkTo(namePokemon.start)
                        bottom.linkTo(parent.bottom, margin = 4.dp)
                        height = Dimension.fillToConstraints
                    }
            ) {
                PokemonTypesItem(types = pokemon.getTypesAsList())
            }

            Image(
                modifier = Modifier
                    .size(96.dp)
                    .alpha(0.25f)
                    .offset(x = 10.dp, y = 10.dp)
                    .constrainAs(pokeballBg) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                painter = painterResource(id = R.drawable.ic_pokeball_empty),
                colorFilter = tint(White),
                contentDescription = null,
            )

            Image(
                painter = rememberImagePainter(pokemon.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .padding(bottom = 8.dp, end = 8.dp)
                    .constrainAs(imagePokemon) {
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                    }
            )
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview(showBackground = false)
@Composable
fun PokemonItemPreview() {
    val context = LocalContext.current
    PokeAppTheme {
        PokemonItem(
            Pokemon(
                1,
                "Bulvieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
                "",
                "fire, grass",
                123,
                12f,
                12f,
                true,
                EMPTY_IMAGE_URI.toString(),
                EMPTY_IMAGE_URI.toString(),
                description = "boulou",
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