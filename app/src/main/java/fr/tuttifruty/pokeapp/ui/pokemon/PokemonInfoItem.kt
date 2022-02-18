package fr.tuttifruty.pokeapp.ui.pokemon

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.ui.common.camera.CameraConstantes.Companion.EMPTY_IMAGE_URI
import fr.tuttifruty.pokeapp.ui.pokemon.section.SectionItem
import fr.tuttifruty.pokeapp.ui.theme.PokeAppTheme
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun PokemonInfoItem(
    modifier: Modifier = Modifier,
    pokemon: Pokemon,
    tabRowHeight: Dp = 74.dp,
    tabRowTopTitlePadding: Dp = 24.dp,
    contentHeight: Dp = 360.dp,
) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val listOfSections = listOf(
        SectionItem.About(pokemon),
        SectionItem.BaseStats(pokemon),
        SectionItem.Evolution(pokemon),
        SectionItem.Moves(pokemon),
    )

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.White,
            contentColor = Color.Black,
            modifier = Modifier
                .height(tabRowHeight)
                .clip(shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }) {
            listOfSections.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(page = index)
                        }
                    },
                ) {
                    Text(
                        modifier = Modifier.padding(top = tabRowTopTitlePadding),
                        text = tab.title
                    )
                }
            }
        }

        HorizontalPager(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .height(contentHeight)
                .background(Color.White),
            state = pagerState,
            count = listOfSections.size,
        ) { page ->
            listOfSections[page].screen()
        }
    }

}

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PokemonInfoItemPreview() {
    PokeAppTheme {
        PokemonInfoItem(
            pokemon = Pokemon(
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
                description = "boulou this a very long description of a pokemon so that we can see what happen on screen"
            ),
        )
    }
}