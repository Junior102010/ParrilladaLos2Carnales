package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminInventoryTabs
import com.edu.ucne.parrilladalos2carnales.presentacion.componentes.AppTopBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import androidx.compose.ui.tooling.preview.Preview
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente

@Composable
fun AdminComponenteListScreen(
    viewModel: AdminComponenteListViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigate: (Screen) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminComponenteListContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToAdd = onNavigateToAdd,
        onNavigateToEdit = onNavigateToEdit,
        onNavigate = onNavigate
    )
}

@Composable
fun AdminComponenteListContent(
    uiState: AdminComponenteListUiState,
    onEvent: (AdminComponenteListUiEvent) -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigate: (Screen) -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Salsas y Términos"
            )
        },
        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = Screen.AdminComponenteList,
                rolUsuario = Rol.ADMINISTRADOR,
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(
                modifier = Modifier.height(14.dp)
            )

            AdminInventoryTabs(
                currentScreen = Screen.AdminComponenteList,
                onNavigate = onNavigate
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.componentes.isEmpty()) {
                    Text("No hay complementos registrados.", modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.componentes) { componente ->
                            val tipoVisible = when (componente.categoriaComponente) {
                                "Salsa" -> "Salsa"
                                "Coccion" -> "Término de carne"
                                else -> componente.categoriaComponente
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToEdit(componente.idComponente) },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = componente.nombreComponente,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Tipo: $tipoVisible | RD$ ${componente.precioComponente}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = if (componente.disponible) "Disponible" else "Agotado",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (componente.disponible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                        )
                                    }
                                    IconButton(onClick = { onEvent(AdminComponenteListUiEvent.OnDeleteComponenteClick(componente)) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Borrar",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminComponenteListPreview() {
    AdminComponenteListContent(
        uiState = AdminComponenteListUiState(
            componentes = listOf(
                Componente(
                    idComponente = 1,
                    nombreComponente = "Salsa BBQ",
                    descripcionComponente = "Salsa BBQ artesanal",
                    cantidadComponente = 10.0,
                    precioComponente = 50.0,
                    disponible = true,
                    coccion = null,
                    categoriaComponente = "Salsa"
                ),
                Componente(
                    idComponente = 2,
                    nombreComponente = "Término Medio",
                    descripcionComponente = "Término de cocción medio",
                    cantidadComponente = 0.0,
                    precioComponente = 0.0,
                    disponible = true,
                    coccion = "Medio",
                    categoriaComponente = "Coccion"
                )
            )
        ),
        onEvent = {},
        onNavigateToAdd = {},
        onNavigateToEdit = {}
    )
}
