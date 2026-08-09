package com.edu.ucne.parrilladalos2carnales.presentacion.carrito

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ResumenCarrito(
    subtotal: Double,
    delivery: Double,
    total: Double
) {

    Surface(
        color =
            MaterialTheme
                .colorScheme
                .surface,

        shape =
            RoundedCornerShape(30.dp),

        border =
            BorderStroke(
                2.dp,
                MaterialTheme
                    .colorScheme
                    .outline
            ),

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(
                    horizontal = 30.dp,
                    vertical = 12.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    5.dp
                )
        ) {

            ResumenFila(
                "Subtotal",
                subtotal
            )

            ResumenFila(
                "Delivery",
                delivery
            )

            ResumenFila(
                "Total",
                total,
                negrita = true
            )
        }
    }
}

@Composable
private fun ResumenFila(
    titulo: String,
    valor: Double,
    negrita: Boolean = false
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = titulo,

            fontWeight =
                if (negrita)
                    FontWeight.Bold
                else
                    FontWeight.Normal,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurface
        )

        Text(
            text =
                String.format(
                    "%.2f",
                    valor
                ),

            fontWeight =
                if (negrita)
                    FontWeight.Bold
                else
                    FontWeight.Normal,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurface
        )
    }
}