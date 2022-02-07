package fr.tuttifruty.pokeapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import fr.tuttifruty.pokeapp.ui.pokedex.PokedexScreen
import fr.tuttifruty.pokeapp.ui.pokemon.PokemonScreen
import fr.tuttifruty.pokeapp.ui.pokemoncapture.PokemonCaptureScreen

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun NavigationComponent() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "pokedex"
    ) {
        composable("pokedex") {
            PokedexScreen(
                navController = navController,
            )
        }
        composable(
            "pokemon/{number}",
            arguments = listOf(
                navArgument("number") { type = NavType.IntType },
                navArgument("name") { nullable = true }
            )
        ) { navBack ->
            PokemonScreen(
                pokemonNumber = navBack.arguments?.getInt("number") ?: 0,
                navController = navController,
            )
        }

        composable(
            "pokemon/{number}/photo",
            arguments = listOf(
                navArgument("number") { type = NavType.IntType },
            )
        ) { navBack ->
            PokemonCaptureScreen(
                pokemonNumber = navBack.arguments?.getInt("number") ?: 0,
                navController = navController,
            )

        }
    }
}
