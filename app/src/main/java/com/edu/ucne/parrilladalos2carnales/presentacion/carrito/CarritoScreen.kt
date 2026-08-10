package com.edu.ucne.parrilladalos2carnales.presentacion.carrito

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.edu.ucne.parrilladalos2carnales.domain.model.carrito.CarritoItem
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import java.io.File

@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel =
        hiltViewModel(),

    onNavigate: (Screen) -> Unit
) {

    val uiState by
    viewModel.uiState
        .collectAsStateWithLifecycle()

    Scaffold(
        containerColor =
            MaterialTheme
                .colorScheme
                .background,

        topBar = {

            Surface(
                color =
                    MaterialTheme
                        .colorScheme
                        .surface,

                shadowElevation = 4.dp
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(
                            WindowInsets.statusBars
                        )
                        .height(52.dp)
                ) {

                    Text(
                        text = "Carrito",

                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurface,

                        modifier = Modifier
                            .align(
                                Alignment.CenterStart
                            )
                            .padding(
                                start = 12.dp
                            )
                    )
                }
            }
        },

        bottomBar = {

            ParrilladaBottomBar(
                currentScreen =
                    Screen.Carrito,

                rolUsuario =
                    Rol.CLIENTE,

                onNavigate =
                    onNavigate
            )
        }
    ) { innerPadding ->

        if (
            uiState.items.isEmpty()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        "Tu carrito está vacío",

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }

        } else {

            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(
                            innerPadding
                        ),

                contentPadding =
                    PaddingValues(
                        start = 10.dp,
                        end = 10.dp,
                        top = 22.dp,
                        bottom = 12.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        22.dp
                    )
            ) {

                items(
                    items =
                        uiState.items,

                    key = {
                        it.idCarritoItem
                    }
                ) { item ->

                    CarritoItemCard(
                        item = item,

                        onIncrementar = {

                            viewModel
                                .incrementar(
                                    item.idCarritoItem
                                )
                        },

                        onDecrementar = {

                            viewModel
                                .decrementar(
                                    item.idCarritoItem
                                )
                        }
                    )
                }

                item {

                    ResumenCarrito(
                        subtotal =
                            uiState.subtotal,

                        delivery =
                            uiState.delivery,

                        total =
                            uiState.total
                    )
                }
                item {

                    Button(
                        onClick = {
                            onNavigate(
                                Screen.Pago
                            )
                        },

                        shape =
                            RoundedCornerShape(
                                28.dp
                            ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {

                        Text(
                            text =
                                "Continuar al pago",

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
