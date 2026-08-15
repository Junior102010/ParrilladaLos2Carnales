package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.edu.ucne.parrilladalos2carnales.R
import kotlin.math.sin

private const val LOGO_INTRO_DURATION = 1200
private val logoIntroEasing = LinearEasing
private const val HALF_ROTATION_RADIANS = 3.14159f

@Composable
fun InicioTopBar() {
    val introProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = LOGO_INTRO_DURATION,
            easing = logoIntroEasing
        ),
        label = "LogoIntro"
    )

    val glowColor = MaterialTheme.colorScheme.primary

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
        shadowElevation = 5.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(105.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.matchParentSize()
                ) {
                    /*
                     * El resplandor aumenta y desaparece
                     * antes de terminar la intro.
                     */
                    val glowProgress = sin(
                        introProgress *
                                HALF_ROTATION_RADIANS
                    ).coerceIn(0f, 1f)


                    drawCircle(
                        color = glowColor.copy(
                            alpha = glowProgress * 0.20f
                        ),
                        radius = size.minDimension * (0.34f + glowProgress * 0.16f),
                        center = center
                    )
                }


                Image(
                    painter = painterResource(
                        id = R.drawable.parrillada_sin_fondo
                    ),
                    contentDescription =
                    "Logo Parrillada Los 2 Carnales",
                    modifier = Modifier
                        .size(58.dp)
                        .graphicsLayer {
                            val progress =
                                introProgress


                            alpha = progress


                            scaleX =
                                0.76f + progress * 0.24f


                            scaleY =
                                0.76f + progress * 0.24f


                            rotationZ =
                                -4f + progress * 4f
                        },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
