package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.list

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminAvailabilitySwitch
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminInventoryTabs
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminSearchField
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminTopBar
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.oferta.AdminOfertaScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.oferta.AdminOfertaViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import java.io.File

import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPlatoListScreen(
    viewModel: AdminPlatoListViewModel,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigate: (Screen) -> Unit = {},
    ofertaViewModel: AdminOfertaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var mostrarOfertas by remember { mutableStateOf(false) }

    if (mostrarOfertas) {
        AdminOfertaScreen(
            viewModel = ofertaViewModel,
            onBack = { mostrarOfertas = false },
            onNavigate = onNavigate
        )
        return
    }

    AdminPlatoListContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToCreate = onNavigateToCreate,
        onNavigateToEdit = onNavigateToEdit,
        onNavigate = onNavigate,
        onVerOfertas = { mostrarOfertas = true }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPlatoListContent(
    uiState: AdminPlatoListUiState,
    onEvent: (AdminPlatoListUiEvent) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigate: (Screen) -> Unit = {},
    onVerOfertas: () -> Unit
) {
    Scaffold(
        topBar = {
            AdminTopBar(
                title = "Gestión de Platos"
            )
        },
        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = Screen.AdminPlatoList,
                rolUsuario = Rol.ADMINISTRADOR,
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Nuevo Plato")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            AdminSearchField(
                value = uiState.searchQuery,
                onValueChange = {
                    onEvent(
                        AdminPlatoListUiEvent.OnSearchQueryChanged(it)
                    )
                },
                placeholder = "Buscar plato...",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminInventoryTabs(
                currentScreen = Screen.AdminPlatoList,
                onNavigate = onNavigate
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onVerOfertas,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ver ofertas",
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.platosFiltrados) { plato ->
                        AdminPlatoCard(
                            plato = plato,
                            onEdit = { onNavigateToEdit(plato.idPlato) },
                            onToggleDisponible = { disponible ->
                                onEvent(
                                    AdminPlatoListUiEvent.OnToggleDisponible(
                                        plato,
                                        disponible
                                    )
                                )
                            },
                            onDelete = {
                                onEvent(
                                    AdminPlatoListUiEvent.OnDeletePlato(plato)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminPlatoListPreview() {
    AdminPlatoListContent(
        uiState = AdminPlatoListUiState(
            platosFiltrados = listOf(
                Plato(
                    idPlato = 1,
                    nombre = "Parrillada Mixta",
                    precio = 1200.0,
                    disponible = true
                ),
                Plato(
                    idPlato = 2,
                    nombre = "Churrasco",
                    precio = 950.0,
                    disponible = false
                )
            )
        ),
        onEvent = {},
        onNavigateToCreate = {},
        onNavigateToEdit = {},
        onVerOfertas = {}
    )
}

@Composable
private fun AdminPlatoCard(
    plato: Plato,
    onEdit: () -> Unit,
    onToggleDisponible: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageModel = remember(plato.imagenUrl) {
                if (plato.imagenUrl.startsWith("/")) File(plato.imagenUrl) else plato.imagenUrl.ifBlank { null }
            }

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                if (imageModel != null) {
                    AsyncImage(
                        model = imageModel,
                        contentDescription = plato.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(text = "🥩", style = MaterialTheme.typography.titleLarge)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plato.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "RD$ ${plato.precio}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            AdminAvailabilitySwitch(
                checked = plato.disponible,
                onCheckedChange = onToggleDisponible
            )

            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Editar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
