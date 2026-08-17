package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta

@Composable
fun OfertaInicioCard(
    oferta: Oferta
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(30.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface.copy(
                    alpha = 0.55f
                )
        ),

        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.outline.copy(
                alpha = 0.55f
            )
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Column(
                modifier = Modifier.align(
                    Alignment.CenterStart
                )
            ) {

                Text(
                    text = oferta.tituloOferta,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "${oferta.descuento.toInt()}% OFF",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
