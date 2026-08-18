package com.edu.ucne.parrilladalos2carnales.presentacion.notificacion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.Notificacion
import com.edu.ucne.parrilladalos2carnales.presentacion.componentes.AppTopBar

@Composable
fun NotificacionesScreen(
    viewModel: NotificacionViewModel,
    onBack: () -> Unit,
    onNotificacionClick: (Notificacion) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Notificaciones",
                onBack = onBack,
                actions = {
                    if (uiState.notificaciones.any { !it.leida }) {
                        IconButton(onClick = { viewModel.marcarTodasLeidas() }) {
                            Icon(Icons.Default.DoneAll, "Marcar todas como leídas")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.notificaciones.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NotificationsNone,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Sin notificaciones",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Cuando tengas novedades aparecerán aquí.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp, top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.notificaciones,
                        key = { it.id }
                    ) { notificacion ->
                        NotificacionCard(
                            notificacion = notificacion,
                            onClick = {
                                viewModel.marcarLeida(notificacion.id)
                                onNotificacionClick(notificacion)
                            }
                        )
                    }
                }
            }
        }
    }
}
