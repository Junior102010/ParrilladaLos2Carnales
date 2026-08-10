package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.edu.ucne.parrilladalos2carnales.R

@Composable
fun InicioTopBar() {
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
            Image(
                painter = painterResource(id = R.drawable.parrillada_sin_fondo),
                contentDescription = "Logo Parrillada Los 2 Carnales",
                modifier = Modifier.size(58.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

