package com.edu.ucne.parrilladalos2carnales.presentacion.seguimiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen

@Composable
fun SeguimientoScreen(
    viewModel: SeguimientoViewModel,
    onNavigate: (Screen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            SeguimientoTopBar()
        },
        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = Screen.Seguimiento(idPedido = 0),
                rolUsuario = Rol.CLIENTE,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.pedido == null -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text(text = uiState.errorMessage ?: "No se encontró el pedido")
                }
            }
            else -> {
                val pedido = uiState.pedido!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    TimelinePedido(estado = pedido.estado)
                    Spacer(Modifier.height(28.dp))
                    MapaPedidoLocal()
                }
            }
        }
    }
}

@Composable
private fun SeguimientoTopBar() {
    Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 4.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(56.dp)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Seguimiento",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun TimelinePedido(estado: EstadoPedido) {
    if (estado == EstadoPedido.CANCELADO) {
        Text(
            text = "Pedido cancelado",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        return
    }

    Column {
        PasoSeguimiento(
            numero = 1,
            titulo = "Recibido",
            icono = Icons.Default.Restaurant,
            completado = estado.paso >= 1,
            actual = estado == EstadoPedido.RECIBIDO
        )
        LineaSeguimiento(completada = estado.paso >= 2)
        PasoSeguimiento(
            numero = 2,
            titulo = "Preparando",
            icono = Icons.Default.SoupKitchen,
            completado = estado.paso >= 2,
            actual = estado == EstadoPedido.PREPARANDO
        )
        LineaSeguimiento(completada = estado.paso >= 3)
        PasoSeguimiento(
            numero = 3,
            titulo = "En Camino",
            icono = Icons.Default.TwoWheeler,
            completado = estado.paso >= 3,
            actual = estado == EstadoPedido.EN_CAMINO
        )
        LineaSeguimiento(completada = estado.paso >= 4)
        PasoSeguimiento(
            numero = 4,
            titulo = "Entregado",
            icono = Icons.Default.Home,
            completado = estado.paso >= 4,
            actual = estado == EstadoPedido.ENTREGADO
        )
    }
}

@Composable
private fun PasoSeguimiento(
    numero: Int,
    titulo: String,
    icono: ImageVector,
    completado: Boolean,
    actual: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = CircleShape,
            color = if (completado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = if (actual) 8.dp else 2.dp,
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icono,
                    contentDescription = titulo,
                    modifier = Modifier.size(32.dp),
                    tint = if (completado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Text(
            text = "$numero. $titulo",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (actual) FontWeight.Bold else FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun LineaSeguimiento(completada: Boolean) {
    Box(
        modifier = Modifier
            .padding(start = 30.dp)
            .width(4.dp)
            .height(38.dp)
            .background(
                color = if (completada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
            )
    )
}

@Composable
private fun MapaPedidoLocal() {
    Card(
        modifier = Modifier.fillMaxWidth().height(230.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = "Mapa temporal",
                modifier = Modifier.size(64.dp),
                tint = Color.Gray
            )
        }
    }
}
