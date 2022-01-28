package fr.tuttifruty.pokeapp.ui.pokedex

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.tuttifruty.pokeapp.ui.theme.PokeAppTheme
import fr.tuttifruty.pokeapp.ui.theme.Transparent


@ExperimentalMaterialApi
@Composable
fun PokemonTypesItem(
    types: List<String>?
) {
    types?.forEach {
        Surface(
            color = Transparent,
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = it,
                fontSize = 10.sp,
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 3.dp,
                    bottom = 3.dp,
                ),
                style = TextStyle(color = Color.Black)
            )
        }
        Spacer(
            modifier = Modifier
                .height(2.dp)
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview(showBackground = false)
@Composable
fun PokemonTypesItemPreview() {
    PokeAppTheme {
        Column {
            PokemonTypesItem(
                listOf("fire", "grass")
            )
        }
    }
}
