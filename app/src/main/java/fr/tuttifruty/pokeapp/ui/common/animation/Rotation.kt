package fr.tuttifruty.pokeapp.ui.common.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun RotateForEver(modifier: Modifier = Modifier, duration: Int, content : @Composable ()-> Unit){
    val infiniteTransition = rememberInfiniteTransition()
    val angleRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation =  tween(
                easing = LinearEasing,
                durationMillis = duration
            )
        )
    )

    Column(modifier = modifier.graphicsLayer { rotationZ = angleRotation }){
        content.invoke()
    }

}