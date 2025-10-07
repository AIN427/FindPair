package com.example.findpairgame.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.findpairgame.R

@Composable
fun RadialGradientBackground(
    modifier: Modifier = Modifier
) {
    val gradientStart = colorResource(R.color.background_gradient_start)
    val gradientEnd = colorResource(R.color.background_gradient_end)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(gradientStart, gradientEnd)
                )
            )
    )
}
