package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminInventoryTabs
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminSearchField
import com.edu.ucne.parrilladalos2carnales.presentacion.componentes.AppTopBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import androidx.compose.ui.tooling.preview.Preview
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion

@Composable
fun AdminGuarnicionListScreen(
    viewModel: AdminGuarnicionListViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigate: (Screen) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminGuarnicionListContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToAdd = onNavigateToAdd,
        onNavigateToEdit = onNavigateToEdit,
        onNavigate = onNavigate
    )
}

@Composable
fun AdminGuarnicionListContent(
    uiState: AdminGuarnicionListUiState,
    onEvent: (AdminGuarnicionListUiEvent) -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigate: (Screen) -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Guarniciones"
            )
        },
        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = Screen.AdminGuarnicionList,
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
                    contentDescription = "Añadir Guarnición"
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

            AdminSearchField(
                value = uiState.searchQuery,
                onValueChange = {
                    onEvent(
                        AdminGuarnicionListUiEvent.OnSearchQueryChanged(it)
                    )
                },
                placeholder = "Buscar guarnición...",
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            AdminInventoryTabs(
                currentScreen = Screen.AdminGuarnicionList,
                onNavigate = onNavigate
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.guarnicionesFiltradas.isEmpty()) {
                    Text(
                        text = if (uiState.searchQuery.isEmpty()) "No hay guarniciones registradas." else "No se encontraron resultados.",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.guarnicionesFiltradas) { guarnicion ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToEdit(guarnicion.idGuarnicion) },
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
                                            text = guarnicion.nombreGuarnicion,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Precio Extra: RD$ ${guarnicion.precioGuarnicion}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = if (guarnicion.disponible) "Disponible" else "Agotado",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (guarnicion.disponible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                        )
                                    }
                                    IconButton(
                                        onClick = { onEvent(AdminGuarnicionListUiEvent.OnDeleteGuarnicionClick(guarnicion)) }
                                    ) {
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
fun AdminGuarnicionListPreview() {
    AdminGuarnicionListContent(
        uiState = AdminGuarnicionListUiState(
            guarnicionesFiltradas = listOf(
                Guarnicion(
                    idGuarnicion = 1,
                    nombreGuarnicion = "Papas Fritas",
                    descripcionGuarnicion = "Papas fritas crocantes",
                    cantidadGuarnicion = 1.0,
                    precioGuarnicion = 100.0,
                    disponible = true,
                    categoria = "Frituras"
                ),
                Guarnicion(
                    idGuarnicion = 2,
                    nombreGuarnicion = "Yuca con Cebolla",
                    descripcionGuarnicion = "Yuca hervida con cebolla",
                    cantidadGuarnicion = 1.0,
                    precioGuarnicion = 80.0,
                    disponible = false,
                    categoria = "Hervidos"
                )
            )
        ),
        onEvent = {},
        onNavigateToAdd = {},
        onNavigateToEdit = {}
    )
}
