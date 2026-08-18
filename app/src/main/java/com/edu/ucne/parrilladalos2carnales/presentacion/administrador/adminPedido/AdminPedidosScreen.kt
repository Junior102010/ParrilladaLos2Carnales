package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminPedido

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminSearchField
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminTopBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen

import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPedidosScreen(
    viewModel: AdminPedidosViewModel = hiltViewModel(),
    onNavigate: (Screen) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminPedidosContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigate = onNavigate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPedidosContent(
    uiState: AdminPedidosUiState,
    onEvent: (AdminPedidosUiEvent) -> Unit,
    onNavigate: (Screen) -> Unit = {}
) {
    val pedidosFiltrados = remember(uiState.pedidos, uiState.filtroEstado, uiState.searchQuery) {
        uiState.pedidos.filter { pedido ->
            val coincideEstado = uiState.filtroEstado == null || pedido.estado == uiState.filtroEstado
            val coincideBusqueda = uiState.searchQuery.isEmpty() ||
                    pedido.clienteNombre.contains(uiState.searchQuery, ignoreCase = true) ||
                    pedido.idPedido.toString().contains(uiState.searchQuery)
            coincideEstado && coincideBusqueda
        }
    }

    Scaffold(
        topBar = {
            AdminTopBar(
                title = "Gestión de Pedidos"
            )
        },
        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = Screen.AdminPedidos,
                rolUsuario = Rol.ADMINISTRADOR,
                onNavigate = onNavigate
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(14.dp))

                AdminSearchField(
                    value = uiState.searchQuery,
                    onValueChange = {
                        onEvent(
                            AdminPedidosUiEvent.OnSearchQueryChanged(it)
                        )
                    },
                    placeholder = "Buscar por cliente o # de orden..."
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        item {
                            FilterChip(
                                selected = uiState.filtroEstado == null,
                                onClick = {
                                    onEvent(
                                        AdminPedidosUiEvent.OnFiltrarPorEstado(null)
                                    )
                                },
                                label = { Text("Todos") },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                        items(EstadoPedido.entries.toTypedArray()) { estado ->
                            FilterChip(
                                selected = uiState.filtroEstado == estado,
                                onClick = {
                                    onEvent(
                                        AdminPedidosUiEvent.OnFiltrarPorEstado(estado)
                                    )
                                },
                                label = { Text(estado.descripcion) },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = { onEvent(AdminPedidosUiEvent.OnRefrescar) },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refrescar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            when {
                uiState.isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(220.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                pedidosFiltrados.isEmpty() -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay pedidos para mostrar",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                else -> {
                    items(
                        pedidosFiltrados,
                        key = { it.idPedido }
                    ) { pedido ->
                        AdminPedidoCard(
                            pedido = pedido,
                            onCambiarEstado = { nuevoEstado ->
                                onEvent(
                                    AdminPedidosUiEvent.OnCambiarEstadoPedido(
                                        pedido.idPedido,
                                        nuevoEstado
                                    )
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminPedidosPreview() {
    AdminPedidosContent(
        uiState = AdminPedidosUiState(
            pedidos = listOf(
                Pedido(
                    idPedido = 1,
                    clienteNombre = "Juan Pérez",
                    total = 1500.0,
                    estado = EstadoPedido.RECIBIDO,
                    fecha = "20/05/2024"
                ),
                Pedido(
                    idPedido = 2,
                    clienteNombre = "María García",
                    total = 850.0,
                    estado = EstadoPedido.PREPARANDO,
                    fecha = "20/05/2024"
                )
            )
        ),
        onEvent = {}
    )
}

@Composable
private fun AdminPedidoCard(
    pedido: Pedido,
    onCambiarEstado: (EstadoPedido) -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    val (badgeBgColor, badgeTextColor) = when (pedido.estado) {
        EstadoPedido.RECIBIDO -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f) to MaterialTheme.colorScheme.tertiary
        EstadoPedido.PREPARANDO -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) to MaterialTheme.colorScheme.primary
        EstadoPedido.EN_CAMINO -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f) to MaterialTheme.colorScheme.secondary
        EstadoPedido.ENTREGADO -> Color(0xFF4CAF50).copy(alpha = 0.2f) to Color(0xFF2E7D32)
        EstadoPedido.CANCELADO -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f) to MaterialTheme.colorScheme.error
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ORDEN #${pedido.idPedido}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = badgeBgColor
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(badgeTextColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = pedido.estado.descripcion,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeTextColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = pedido.clienteNombre.ifBlank { "Cliente #${pedido.idUsuario}" },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = pedido.fecha,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Outlined.ShoppingBag,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Para llevar",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val resumenPlatos = if (pedido.detalles.isNotEmpty()) {
                pedido.detalles.joinToString(", ") { "${it.cantidad}x ${it.nombrePlato}" }
            } else {
                "Sin detalle especificado"
            }

            Text(
                text = resumenPlatos,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RD$ ${"%.2f".format(pedido.total)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Box {
                    OutlinedButton(
                        onClick = { expandedMenu = true },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = pedido.estado.descripcion,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Cambiar estado"
                        )
                    }

                    DropdownMenu(
                        expanded = expandedMenu,
                        onDismissRequest = { expandedMenu = false }
                    ) {
                        EstadoPedido.entries.forEach { estado ->
                            DropdownMenuItem(
                                text = { Text(estado.descripcion) },
                                onClick = {
                                    onCambiarEstado(estado)
                                    expandedMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
