package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import com.edu.ucne.parrilladalos2carnales.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

@Composable
fun ProductoInicioCard(
    plato: Plato,
    onClick: () -> Unit,
    onAddClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(155.dp)
            .height(155.dp),
        shape = RoundedCornerShape(30.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface.copy(
                    alpha = 0.50f
                )
        ),

        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.outline.copy(
                alpha = 0.45f
            )
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            if (plato.imagenUrl.isNotBlank()) {

                AsyncImage(
                    model = plato.imagenUrl,
                    contentDescription = plato.nombre,
                    modifier = Modifier
                        .size(92.dp)
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.Crop
                )

            } else {

                Image(
                    painter = painterResource(
                        id = R.drawable.parrillada_sin_fondo
                    ),
                    contentDescription = plato.nombre,
                    modifier = Modifier
                        .size(85.dp)
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        start = 10.dp,
                        bottom = 8.dp
                    )
            ) {

                Text(
                    text = plato.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                Text(
                    text = "RD$ ${String.format("%.2f", plato.precio)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = onAddClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription =
                        "Agregar ${plato.nombre}",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}