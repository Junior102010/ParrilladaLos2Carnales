package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import com.edu.ucne.parrilladalos2carnales.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun InicioTopBar(
    onPerfilClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(
            bottomStart = 25.dp,
            bottomEnd = 25.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        shadowElevation = 5.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(105.dp),
            contentAlignment = Alignment.Center
        ) {

            // Logo central
            Image(
                painter = painterResource(
                    id = R.drawable.parrillada_sin_fondo
                ),
                contentDescription = "Logo Parrillada Los 2 Carnales",
                modifier = Modifier.size(58.dp),
                contentScale = ContentScale.Fit
            )

            // Perfil y menú
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onPerfilClick
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Perfil",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = {
                        // Más adelante:
                        // menú de opciones
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Más opciones",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}