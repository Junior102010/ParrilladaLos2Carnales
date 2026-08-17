package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.OutdoorGrill
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.SetMeal
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria

@Composable
fun CategoriasInicio(
    categorias: List<Categoria>,
    onCategoriaClick: (Categoria) -> Unit
) {
    if (categorias.isEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CategoriaInicioItem(
                nombre = "Parrillada",
                icono = Icons.Outlined.OutdoorGrill,
                onClick = { onCategoriaClick(Categoria(nombreCategoria = "Parrillada")) }
            )
            CategoriaInicioItem(
                nombre = "Cortes",
                icono = Icons.Outlined.SetMeal,
                onClick = { onCategoriaClick(Categoria(nombreCategoria = "Cortes")) }
            )
            CategoriaInicioItem(
                nombre = "Bebidas",
                icono = Icons.Outlined.LocalBar,
                onClick = { onCategoriaClick(Categoria(nombreCategoria = "Bebidas")) }
            )
        }
    } else {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(categorias) { categoria ->
                val icono = when (categoria.nombreCategoria.lowercase()) {
                    "parrilladas" -> Icons.Outlined.OutdoorGrill
                    "cortes" -> Icons.Outlined.SetMeal
                    "bebidas" -> Icons.Outlined.LocalBar
                    else -> Icons.Outlined.Restaurant
                }
                CategoriaInicioItem(
                    nombre = categoria.nombreCategoria,
                    icono = icono,
                    onClick = { onCategoriaClick(categoria) }
                )
            }
        }
    }
}
