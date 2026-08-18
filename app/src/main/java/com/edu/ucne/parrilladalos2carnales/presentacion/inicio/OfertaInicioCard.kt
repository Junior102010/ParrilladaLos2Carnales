package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import java.io.File

@Composable
fun OfertaInicioCard(
    oferta: Oferta
) {
    val imageModel = remember(oferta.imagenUrl) {
        if (oferta.imagenUrl.startsWith("/")) File(oferta.imagenUrl) else oferta.imagenUrl
    }

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // IMAGEN DE FONDO
            AsyncImage(
                model = imageModel,
                contentDescription = oferta.tituloOferta,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // OVERLAY OSCURO PARA LEGIBILIDAD
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f
                        )
                    )
            )

            // CONTENIDO
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        horizontal = 22.dp,
                        vertical = 20.dp
                    ),
                verticalArrangement = Arrangement.Bottom // Changed to Bottom to fit the gradient better
            ) {
                Text(
                    text = oferta.tituloOferta,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White, // White text for the dark overlay
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (oferta.descripcionOferta.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = oferta.descripcionOferta,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 10.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "${oferta.descuento.toInt()}% OFF",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}
