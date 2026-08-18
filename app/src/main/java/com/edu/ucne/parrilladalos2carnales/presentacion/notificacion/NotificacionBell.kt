package com.edu.ucne.parrilladalos2carnales.presentacion.notificacion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NotificacionBell(
    cantidad: Int,
    onClick: () -> Unit
) {

    Box {

        IconButton(
            onClick = onClick
        ) {

            Icon(
                imageVector =
                    if (cantidad > 0) {
                        Icons.Filled.Notifications
                    } else {
                        Icons.Outlined.Notifications
                    },

                contentDescription =
                    "Notificaciones",

                tint =
                    MaterialTheme
                        .colorScheme
                        .onSurface
            )
        }

        if (cantidad > 0) {

            Surface(
                modifier =
                    Modifier
                        .align(
                            Alignment.TopEnd
                        )
                        .size(18.dp),

                shape =
                    CircleShape,

                color =
                    MaterialTheme
                        .colorScheme
                        .primary
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            if (cantidad > 9) {
                                "9+"
                            } else {
                                cantidad.toString()
                            },

                        style =
                            MaterialTheme
                                .typography
                                .labelSmall,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onPrimary
                    )
                }
            }
        }
    }
}
