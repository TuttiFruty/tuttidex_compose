package fr.tuttifruty.pokeapp.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Loading(){
    Box(
        contentAlignment = Alignment.Center, // you apply alignment to all children
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center) // or to a specific child
        )
    }
}