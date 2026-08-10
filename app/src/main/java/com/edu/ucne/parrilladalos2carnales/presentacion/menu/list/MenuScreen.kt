package com.edu.ucne.parrilladalos2carnales.presentacion.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.edu.ucne.parrilladalos2carnales.R
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.menu.list.funciones.MenuTopBar
import com.edu.ucne.parrilladalos2carnales.presentacion.menu.list.funciones.PlatoMenuCard
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import com.edu.ucne.parrilladalos2carnales.presentacion.plato.list.PlatoListUiEvent
import com.edu.ucne.parrilladalos2carnales.presentacion.plato.list.PlatoListViewModel

@Composable
fun MenuScreen(
    viewModel: PlatoListViewModel = hiltViewModel(),
    onNavigate: (Screen) -> Unit,
    onPlatoClick: (Int) -> Unit
) {

    val uiState by
    viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor =
            MaterialTheme.colorScheme.background,

        topBar = {
            MenuTopBar()
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
                    viewModel.onEvent(
                        PlatoListUiEvent.OnSearchChange(it)
                    )
                },

                placeholder = {
                    Text(
                        text = "Buscar",
                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint =
                            MaterialTheme.colorScheme
                                .onSurface
                    )
                },

                trailingIcon = {

                    if (uiState.searchQuery.isNotBlank()) {

                        IconButton(
                            onClick = {

                                viewModel.onEvent(
                                    PlatoListUiEvent
                                        .OnSearchChange("")
                                )
                            }
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Close,
                                contentDescription =
                                    "Limpiar búsqueda"
                            )
                        }
                    }
                },

                singleLine = true,

                shape =
                    RoundedCornerShape(28.dp),

                colors =
                    OutlinedTextFieldDefaults.colors(

                        focusedContainerColor =
                            MaterialTheme.colorScheme
                                .surfaceVariant,

                        unfocusedContainerColor =
                            MaterialTheme.colorScheme
                                .surfaceVariant,

                        focusedBorderColor =
                            MaterialTheme.colorScheme
                                .outline,

                        unfocusedBorderColor =
                            MaterialTheme.colorScheme
                                .outlineVariant,

                        focusedTextColor =
                            MaterialTheme.colorScheme
                                .onSurface,

                        unfocusedTextColor =
                            MaterialTheme.colorScheme
                                .onSurface
                    ),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .padding(
                        horizontal = 18.dp,
                        vertical = 6.dp
                    )
            )

            when {

                uiState.isLoading -> {

                    Box(
                        modifier =
                            Modifier.fillMaxSize(),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            color =
                                MaterialTheme
                                    .colorScheme.primary
                        )
                    }
                }

                uiState.platosFiltrados.isEmpty() -> {

                    Box(
                        modifier =
                            Modifier.fillMaxSize(),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text =
                                if (
                                    uiState.searchQuery
                                        .isBlank()
                                ) {
                                    "No hay platos disponibles"
                                } else {
                                    "No se encontraron platos"
                                },

                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                        )
                    }
                }

                else -> {

                    LazyVerticalGrid(
                        columns =
                            GridCells.Fixed(2),

                        contentPadding =
                            PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 10.dp,
                                bottom = 16.dp
                            ),

                        horizontalArrangement =
                            Arrangement.spacedBy(
                                20.dp
                            ),

                        verticalArrangement =
                            Arrangement.spacedBy(
                                26.dp
                            ),

                        modifier =
                            Modifier.fillMaxSize()
                    ) {

                        items(
                            items =
                                uiState.platosFiltrados,

                            key = {
                                it.idPlato
                            }
                        ) { plato ->

                            PlatoMenuCard(
                                plato = plato,

                                onClick = {
                                    onPlatoClick(
                                        plato.idPlato
                                    )
                                },

                                onAddToCart = {
                                    onPlatoClick(
                                        plato.idPlato
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
