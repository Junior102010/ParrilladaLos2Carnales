package com.edu.ucne.parrilladalos2carnales.presentacion.menu.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.menu.list.funciones.PlatoMenuCard
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import com.edu.ucne.parrilladalos2carnales.presentacion.plato.list.PlatoListUiEvent
import com.edu.ucne.parrilladalos2carnales.presentacion.plato.list.PlatoListViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.edu.ucne.parrilladalos2carnales.presentacion.componentes.AppTopBar
import com.edu.ucne.parrilladalos2carnales.presentacion.plato.list.PlatoListUiState

@Composable
fun MenuScreen(
    viewModel: PlatoListViewModel = hiltViewModel(),
    titulo: String = "Menú",
    onNavigate: (Screen) -> Unit,
    onPlatoClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MenuContent(
        uiState = uiState,
        titulo = titulo,
        onEvent = viewModel::onEvent,
        onNavigate = onNavigate,
        onPlatoClick = onPlatoClick
    )
}

@Composable
fun MenuContent(
    uiState: PlatoListUiState,
    titulo: String,
    onEvent: (PlatoListUiEvent) -> Unit,
    onNavigate: (Screen) -> Unit,
    onPlatoClick: (Int) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(title = titulo)
        },
        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = Screen.Menu,
                rolUsuario = Rol.CLIENTE,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = {
                    onEvent(PlatoListUiEvent.OnSearchChange(it))
                },
                placeholder = {
                    Text(
                        text = "Buscar",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotBlank()) {
                        IconButton(
                            onClick = {
                                onEvent(PlatoListUiEvent.OnSearchChange(""))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            )

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                uiState.platosFiltrados.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestaurantMenu,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (uiState.searchQuery.isBlank()) {
                                "Actualmente no hay productos en esta categoría"
                            } else {
                                "No se encontraron platos"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Puedes explorar otra categoría o ver el menú completo.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        
                        if (uiState.searchQuery.isNotBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { onEvent(PlatoListUiEvent.OnSearchChange("")) }) {
                                Text("Ver todo el menú")
                            }
                        }
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 10.dp,
                            bottom = 16.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalArrangement = Arrangement.spacedBy(26.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = uiState.platosFiltrados,
                            key = { it.idPlato }
                        ) { plato ->
                            PlatoMenuCard(
                                plato = plato,
                                onClick = { onPlatoClick(plato.idPlato) },
                                onAddToCart = { onPlatoClick(plato.idPlato) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    MenuContent(
        uiState = PlatoListUiState(
            platosFiltrados = emptyList()
        ),
        titulo = "Menú",
        onEvent = {},
        onNavigate = {},
        onPlatoClick = {}
    )
}
