package com.edu.ucne.parrilladalos2carnales.presentacion.historial

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistorialScreen(
    viewModel: HistorialViewModel = hiltViewModel(),
    onNavigate: (Screen) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refrescarHistorial()
    }

    LaunchedEffect(uiState.pedidoRepetidoExitosamente) {
        if (uiState.pedidoRepetidoExitosamente) {
            viewModel.onMensajeConsumido()
            onNavigate(Screen.Carrito)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { HistorialTopBar(onBack = onBack, onPerfilClick = { onNavigate(Screen.Perfil) }) }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize().padding(innerPadding), Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            uiState.errorMessage != null -> {
                Box(Modifier.fillMaxSize().padding(innerPadding).padding(24.dp), Alignment.Center) {
                    Text(text = uiState.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
                }
            }
            uiState.pedidos.isEmpty() -> {
                HistorialVacio(Modifier.fillMaxSize().padding(innerPadding))
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items = uiState.pedidos, key = { it.idPedido }) { pedido ->
                        HistorialPedidoCard(
                            pedido = pedido,
                            onRepetir = { viewModel.repetirPedido(pedido) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistorialTopBar(onBack: () -> Unit, onPerfilClick: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).height(56.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onSurface)
            }
            Text("Mi Historial", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.align(Alignment.Center))
            IconButton(onClick = onPerfilClick, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(Icons.Default.Person, "Perfil", tint = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun HistorialPedidoCard(pedido: Pedido, onRepetir: () -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column {
                    Text(pedido.numeroOrden, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(formatearFecha(pedido.fechaMillis), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                EstadoPedidoChip(pedido.estado)
            }
            HorizontalDivider(Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            
            pedido.detalles.forEach { detalle ->
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("${detalle.cantidad}x ${detalle.nombrePlato}", Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("RD$ ${String.format("%.2f", detalle.subtotal)}")
                }
                val config = listOfNotNull(detalle.termino.takeIf { it.isNotBlank() }, detalle.guarnicion.takeIf { it.isNotBlank() }, detalle.salsa.takeIf { it.isNotBlank() }).joinToString(" • ")
                if (config.isNotBlank()) {
                    Text(config, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column {
                    Text("Total", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("RD$ ${String.format("%.2f", pedido.total)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Button(
                    onClick = onRepetir,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Replay, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Repetir", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun EstadoPedidoChip(estado: EstadoPedido) {
    val color = when (estado) {
        EstadoPedido.RECIBIDO -> MaterialTheme.colorScheme.tertiary
        EstadoPedido.PREPARANDO -> MaterialTheme.colorScheme.primary
        EstadoPedido.EN_CAMINO -> MaterialTheme.colorScheme.secondary
        EstadoPedido.ENTREGADO -> Color(0xFF4CAF50)
        EstadoPedido.CANCELADO -> MaterialTheme.colorScheme.error
    }
    Surface(color = color.copy(alpha = 0.15f), contentColor = color, shape = RoundedCornerShape(20.dp)) {
        Text(estado.descripcion, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun HistorialVacio(modifier: Modifier = Modifier) {
    Column(modifier.padding(32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.ReceiptLong, null, Modifier.size(70.dp), tint = MaterialTheme.colorScheme.outlineVariant)
        Spacer(Modifier.height(16.dp))
        Text("Aún no tienes pedidos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text("Cuando realices un pedido aparecerá aquí.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun formatearFecha(millis: Long): String {
    if (millis <= 0L) return ""
    val formato = SimpleDateFormat("dd/MM/yyyy • hh:mm a", Locale.getDefault())
    return formato.format(Date(millis))
}
