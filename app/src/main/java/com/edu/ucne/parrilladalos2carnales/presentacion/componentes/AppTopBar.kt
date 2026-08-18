package com.edu.ucne.parrilladalos2carnales.presentacion.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.edu.ucne.parrilladalos2carnales.R
import com.edu.ucne.parrilladalos2carnales.presentacion.notificacion.NotificacionBell

@Composable
fun AppTopBar(
    title: String,
    showLogo: Boolean = false,
    onBack: (() -> Unit)? = null,
    cantidadNotificaciones: Int = 0,
    onNotificacionesClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
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
            color = MaterialTheme.colorScheme.outline.copy(
                alpha = 0.55f
            )
        ),
        shadowElevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(58.dp)
                .padding(
                    start = if (onBack != null) 4.dp else 16.dp,
                    end = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (onBack != null) {
                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (showLogo) {
                Image(
                    painter = painterResource(
                        id = R.drawable.parrillada_sin_fondo
                    ),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(38.dp)
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            actions()

            if (onNotificacionesClick != null) {
                NotificacionBell(
                    cantidad = cantidadNotificaciones,
                    onClick = onNotificacionesClick
                )
            }
        }
    }
}
