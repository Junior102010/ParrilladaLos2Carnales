package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CategoriaInicioItem(
    nombre: String,
    icono: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        onClick = onClick,
        modifier = modifier,
        color = Color.Transparent
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            Icon(
                imageVector = icono,
                contentDescription = nombre,

                modifier =
                    Modifier.size(34.dp),

                tint =
                    MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier =
                    Modifier.height(7.dp)
            )

            Text(
                text = nombre,

                style =
                    MaterialTheme.typography.labelLarge,

                fontWeight =
                    FontWeight.SemiBold,

                color =
                    MaterialTheme.colorScheme.onBackground,

                textAlign =
                    TextAlign.Center,

                maxLines = 1,

                overflow =
                    TextOverflow.Ellipsis
            )
        }
    }
}
