package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
        return
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(
            items = categorias,
            key = { it.idCategoria }
        ) { categoria ->
            val icono = when (categoria.nombreCategoria.trim().lowercase()) {
                "parrillada", "parrilladas" -> Icons.Outlined.OutdoorGrill
                "corte", "cortes" -> Icons.Outlined.SetMeal
                "bebida", "bebidas" -> Icons.Outlined.LocalBar
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
