package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.OutdoorGrill
import androidx.compose.material.icons.outlined.SetMeal
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CategoriasInicio(
    onCategoriaClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Top
    ) {

        CategoriaInicioItem(
            nombre = "Parrillada",
            icono = Icons.Outlined.OutdoorGrill,
            onClick = onCategoriaClick
        )

        CategoriaInicioItem(
            nombre = "Cortes",
            icono = Icons.Outlined.SetMeal,
            onClick = onCategoriaClick
        )

        CategoriaInicioItem(
            nombre = "Bebidas",
            icono = Icons.Outlined.LocalBar,
            onClick = onCategoriaClick
        )
    }
}
