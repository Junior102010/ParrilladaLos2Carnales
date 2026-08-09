package com.edu.ucne.parrilladalos2carnales.presentacion.menu.list.funciones

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun MenuTopBar(
    onPerfilClick: () -> Unit
) {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        color =
            MaterialTheme.colorScheme.surface,

        shadowElevation = 5.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(
                    WindowInsets.statusBars
                )
                .height(48.dp)
        ) {

            Text(
                text = "Menú",

                style =
                    MaterialTheme.typography
                        .headlineSmall,

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme.colorScheme
                        .onSurface,

                modifier = Modifier
                    .align(
                        Alignment.CenterStart
                    )
                    .padding(start = 12.dp)
            )

            Row(
                modifier = Modifier
                    .align(
                        Alignment.CenterEnd
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onPerfilClick
                ) {

                    Icon(
                        imageVector =
                            Icons.Default
                                .AccountCircle,

                        contentDescription =
                            "Perfil",

                        tint =
                            MaterialTheme
                                .colorScheme
                                .onSurface,

                        modifier =
                            Modifier.size(28.dp)
                    )
                }

                IconButton(
                    onClick = {
                        // Luego podemos poner
                        // DropdownMenu.
                    }
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.MoreVert,

                        contentDescription =
                            "Más opciones",

                        tint =
                            MaterialTheme
                                .colorScheme
                                .onSurface
                    )
                }
            }
        }
    }
}