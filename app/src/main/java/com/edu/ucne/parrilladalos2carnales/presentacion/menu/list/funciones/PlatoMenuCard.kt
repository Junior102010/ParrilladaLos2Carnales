package com.edu.ucne.parrilladalos2carnales.presentacion.menu.list.funciones

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import com.edu.ucne.parrilladalos2carnales.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato


@Composable
fun PlatoMenuCard(
    plato: Plato,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {

    Card(
        onClick = onClick,

        shape =
            RoundedCornerShape(30.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .surface
                        .copy(
                            alpha = 0.55f
                        )
            ),

        border =
            BorderStroke(
                width = 2.dp,
                color =
                    MaterialTheme
                        .colorScheme
                        .outline
                        .copy(
                            alpha = 0.50f
                        )
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            ),

        modifier = Modifier
            .fillMaxWidth()
            .height(154.dp)
    ) {

        Box(
            modifier =
                Modifier.fillMaxSize()
        ) {

            if (
                plato.imagenUrl.isNotBlank()
            ) {

                AsyncImage(
                    model =
                        plato.imagenUrl,

                    contentDescription =
                        plato.nombre,

                    contentScale =
                        ContentScale.Crop,

                    modifier = Modifier
                        .size(92.dp)
                        .align(
                            Alignment.TopCenter
                        )
                        .padding(top = 8.dp)
                )

            } else {

                Image(
                    painter =
                        painterResource(
                            id =
                                R.drawable
                                    .parrillada_sin_fondo
                        ),

                    contentDescription =
                        plato.nombre,

                    contentScale =
                        ContentScale.Fit,

                    modifier = Modifier
                        .size(88.dp)
                        .align(
                            Alignment.TopCenter
                        )
                        .padding(top = 8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(
                        Alignment.BottomStart
                    )
                    .padding(
                        start = 10.dp,
                        bottom = 7.dp
                    )
            ) {

                Text(
                    text = plato.nombre,

                    style =
                        MaterialTheme.typography
                            .bodyLarge,

                    color =
                        MaterialTheme.colorScheme
                            .onSurface,

                    maxLines = 1,

                    overflow =
                        TextOverflow.Ellipsis,

                    modifier =
                        Modifier.width(100.dp)
                )

                Text(
                    text =
                        "RD$ ${
                            String.format(
                                "%.2f",
                                plato.precio
                            )
                        }",

                    style =
                        MaterialTheme.typography
                            .bodyMedium,

                    color =
                        MaterialTheme.colorScheme
                            .onSurface
                )
            }

            IconButton(
                onClick =
                    onAddToCart,

                modifier = Modifier
                    .align(
                        Alignment.BottomEnd
                    )
                    .size(44.dp)
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Add,

                    contentDescription =
                        "Agregar ${plato.nombre}",

                    tint =
                        MaterialTheme
                            .colorScheme
                            .onSurface,

                    modifier =
                        Modifier.size(28.dp)
                )
            }
        }
    }
}
