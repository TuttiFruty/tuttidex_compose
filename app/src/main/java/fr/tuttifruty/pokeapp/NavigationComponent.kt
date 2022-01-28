package fr.tuttifruty.pokeapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.ui.common.ChildScreen
import fr.tuttifruty.pokeapp.ui.common.ParentScreen
import fr.tuttifruty.pokeapp.ui.pokedex.PokedexScreen
import fr.tuttifruty.pokeapp.ui.pokemon.PokemonScreen

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
            ParentScreen(title = "Pokedex") {
                PokedexScreen(navController = navController)
            }
        }
        composable(
            "pokemon/{number}?name={name}",
            arguments = listOf(
                navArgument("number") { type = NavType.IntType },
                navArgument("name") { nullable = true }
            )
        ) { navBack ->
            val number = navBack.arguments?.getInt("number") ?: 0
            val numberTitle = stringResource(R.string.number, Pokemon.getNumberStandardized(number))
            val name = navBack.arguments?.getString("name") ?: ""
            ChildScreen(title = "$numberTitle $name", navController = navController) {
                PokemonScreen(number)
            }
        }
    }
}
