package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminPedido


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente.AdminDrawerContent
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPedidosScreen(
    viewModel: AdminPedidosViewModel = hiltViewModel(),
    onNavigate: (Screen) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pedidosFiltrados = remember(uiState.pedidos, uiState.filtroEstado, uiState.searchQuery) {
        uiState.pedidos.filter { pedido ->
            val coincideEstado = uiState.filtroEstado == null || pedido.estado == uiState.filtroEstado
            val coincideBusqueda = uiState.searchQuery.isEmpty() ||
                    pedido.clienteNombre.contains(uiState.searchQuery, ignoreCase = true) ||
                    pedido.idPedido.toString().contains(uiState.searchQuery)
            coincideEstado && coincideBusqueda
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AdminDrawerContent(
                currentScreen = Screen.AdminGuarnicionList,
                onNavigate = onNavigate,
                onLogout = { onNavigate(Screen.Login) },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
        ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Gestión de Pedidos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Notificaciones */ }) {
                            Box {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notificaciones",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(26.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .align(Alignment.TopEnd)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.error)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {


                Text(
                    text = "Supervisa y actualiza el estado de las órdenes en tiempo real.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                // Buscador
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onEvent(AdminPedidosUiEvent.OnSearchQueryChanged(it)) },
                    placeholder = {
                        Text(
                            "Buscar orden por cliente o # ID...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                viewModel.onEvent(
                                    AdminPedidosUiEvent.OnSearchQueryChanged(
                                        ""
                                    )
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar"
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                // Filtros y Refrescar
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
                                    viewModel.onEvent(
                                        AdminPedidosUiEvent.OnFiltrarPorEstado(
                                            null
                                        )
                                    )
                                },
                                label = { Text("Todos") },
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                        items(EstadoPedido.entries.toTypedArray()) { estado ->
                            FilterChip(
                                selected = uiState.filtroEstado == estado,
                                onClick = {
                                    viewModel.onEvent(
                                        AdminPedidosUiEvent.OnFiltrarPorEstado(
                                            estado
                                        )
                                    )
                                },
                                label = { Text(estado.descripcion) },
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.onEvent(AdminPedidosUiEvent.OnRefrescar) },
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

                // Lista o Estado Vacío
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (pedidosFiltrados.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ReceiptLong,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "No hay pedidos registrados",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Cuando los clientes realicen órdenes, aparecerán aquí automáticamente para ser gestionadas.",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                OutlinedButton(
                                    onClick = { viewModel.onEvent(AdminPedidosUiEvent.OnRefrescar) },
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Refrescar lista")
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(pedidosFiltrados, key = { it.idPedido }) { pedido ->
                            AdminPedidoCard(
                                pedido = pedido,
                                onCambiarEstado = { nuevoEstado ->
                                    viewModel.onEvent(
                                        AdminPedidosUiEvent.OnCambiarEstadoPedido(
                                            pedido.idPedido,
                                            nuevoEstado
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminPedidoCard(
    pedido: Pedido,
    onCambiarEstado: (EstadoPedido) -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    val (badgeBgColor, badgeTextColor) = when (pedido.estado) {
        EstadoPedido.PENDIENTE -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f) to MaterialTheme.colorScheme.tertiary
        EstadoPedido.EN_PROCESO -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) to MaterialTheme.colorScheme.primary
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
                text = if (pedido.clienteNombre.isBlank()) "Cliente #${pedido.idUsuario}" else pedido.clienteNombre,
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