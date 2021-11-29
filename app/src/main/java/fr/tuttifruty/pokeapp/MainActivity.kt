package fr.tuttifruty.pokeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import fr.tuttifruty.pokeapp.ui.pokedex.Pokedex
import fr.tuttifruty.pokeapp.ui.pokedex.PokedexScreen
import fr.tuttifruty.pokeapp.ui.pokedex.PokedexViewModel
import fr.tuttifruty.pokeapp.ui.theme.PokeAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    val pokedexViewModel: PokedexViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PokedexScreen(pokedexViewModel)
                }
            }
        }
    }
}
