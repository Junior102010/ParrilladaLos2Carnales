package com.edu.ucne.parrilladalos2carnales.presentacion.carrito

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.edu.ucne.parrilladalos2carnales.domain.model.carrito.CarritoItem
import java.io.File


@Composable
fun CarritoItemCard(
    item: CarritoItem,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit
) {

    val imageModel =
        if (
            item.plato.imagenUrl
                .startsWith("/")
        ) {
            File(
                item.plato.imagenUrl
            )
        } else {
            item.plato.imagenUrl
        }

    Card(
        shape =
            RoundedCornerShape(28.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .surfaceVariant
            ),

        border =
            BorderStroke(
                width = 1.dp,
                color =
                    MaterialTheme
                        .colorScheme
                        .outlineVariant
            ),

        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
    ) {

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(10.dp)
        ) {

            AsyncImage(
                model = imageModel,

                contentDescription =
                    item.plato.nombre,

                contentScale =
                    ContentScale.Crop,

                modifier = Modifier
                    .size(76.dp)
                    .align(
                        Alignment.TopStart
                    )
            )

            Column(
                modifier = Modifier
                    .align(
                        Alignment.TopStart
                    )
                    .padding(
                        start = 82.dp,
                        end = 55.dp
                    )
            ) {

                Text(
                    text =
                        item.plato.nombre,

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,

                    fontWeight =
                        FontWeight.SemiBold,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurface,

                    maxLines = 1,

                    overflow =
                        TextOverflow.Ellipsis
                )

                Spacer(
                    Modifier.height(4.dp)
                )

                Text(
                    text =
                        configuracionTexto(
                            item
                        ),

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,

                    maxLines = 4
                )
            }

            Text(
                text =
                    "RD$ ${
                        String.format(
                            "%.2f",
                            item.subtotal
                        )
                    }",

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface,

                modifier =
                    Modifier.align(
                        Alignment.TopEnd
                    )
            )

            Surface(
                color =
                    MaterialTheme
                        .colorScheme
                        .outlineVariant,

                shape =
                    RoundedCornerShape(28.dp),

                modifier = Modifier
                    .align(
                        Alignment.BottomEnd
                    )
                    .height(40.dp)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick =
                            onDecrementar,

                        modifier =
                            Modifier.size(36.dp)
                    ) {

                        Icon(
                            Icons.Default.Remove,
                            "Disminuir"
                        )
                    }

                    Text(
                        text =
                            item.cantidad
                                .toString(),

                        color =
                            MaterialTheme
                                .colorScheme
                                .primary,

                        fontWeight =
                            FontWeight.Bold
                    )

                    IconButton(
                        onClick =
                            onIncrementar,

                        modifier =
                            Modifier.size(36.dp)
                    ) {

                        Icon(
                            Icons.Default.Add,
                            "Aumentar"
                        )
                    }
                }
            }
        }
    }
}

private fun configuracionTexto(
    item: CarritoItem
): String {


    val partes =
        mutableListOf<String>()


    item.termino
        ?.nombreComponente
        ?.takeIf {
            it.isNotBlank()
        }
        ?.let {
            partes.add(
                "Término: $it"
            )
        }


    item.guarnicion
        ?.nombreGuarnicion
        ?.takeIf {
            it.isNotBlank()
        }
        ?.let {
            partes.add(
                "Guarnición: $it"
            )
        }


    item.salsa
        ?.nombreComponente
        ?.takeIf {
            it.isNotBlank()
        }
        ?.let {
            partes.add(
                "Salsa: $it"
            )
        }


    if (
        item.guarnicionesExtra
            .isNotEmpty()
    ) {


        partes.add(
            "Extras: ${
                item.guarnicionesExtra
                    .joinToString(", ") {
                        it.nombreGuarnicion
                    }
            }"
        )
    }


    if (
        item.salsasExtra
            .isNotEmpty()
    ) {


        partes.add(
            "Salsas extra: ${
                item.salsasExtra
                    .joinToString(", ") {
                        it.nombreComponente
                    }
            }"
        )
    }


    return partes.joinToString(
        " | "
    )
}
