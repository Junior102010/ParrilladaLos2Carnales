package com.edu.ucne.parrilladalos2carnales.presentacion.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.domain.model.carrito.CarritoItem
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import androidx.compose.ui.tooling.preview.Preview
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.presentacion.componentes.AppTopBar

@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel = hiltViewModel(),
    onNavigate: (Screen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CarritoContent(
        uiState = uiState,
        onIncrementar = { viewModel.incrementar(it) },
        onDecrementar = { viewModel.decrementar(it) },
        onNavigate = onNavigate
    )
}

@Composable
fun CarritoContent(
    uiState: CarritoUiState,
    onIncrementar: (Long) -> Unit,
    onDecrementar: (Long) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(title = "Carrito")
        },
        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = Screen.Carrito,
                rolUsuario = Rol.CLIENTE,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        if (uiState.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tu carrito está vacío",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = 10.dp,
                    end = 10.dp,
                    top = 22.dp,
                    bottom = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                items(
                    items = uiState.items,
                    key = { it.idCarritoItem }
                ) { item ->
                    CarritoItemCard(
                        item = item,
                        onIncrementar = { onIncrementar(item.idCarritoItem) },
                        onDecrementar = { onDecrementar(item.idCarritoItem) }
                    )
                }

                item {
                    ResumenCarrito(
                        subtotal = uiState.subtotal,
                        delivery = uiState.delivery,
                        total = uiState.total
                    )
                }
                item {
                    Button(
                        onClick = { onNavigate(Screen.Pago) },
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = "Continuar al pago",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarritoPreview() {
    CarritoContent(
        uiState = CarritoUiState(
            items = listOf(
                CarritoItem(
                    idCarritoItem = 1L,
                    plato = Plato(
                        idPlato = 1,
                        nombre = "Parrillada Mixta",
                        precio = 1200.0,
                        disponible = true
                    ),
                    termino = null,
                    guarnicion = null,
                    salsa = null,
                    cantidad = 1,
                    precioUnitario = 1200.0
                ),
                CarritoItem(
                    idCarritoItem = 2L,
                    plato = Plato(
                        idPlato = 2,
                        nombre = "Churrasco",
                        precio = 950.0,
                        disponible = true
                    ),
                    termino = null,
                    guarnicion = null,
                    salsa = null,
                    cantidad = 2,
                    precioUnitario = 950.0
                )
            ),
            subtotal = 3100.0,
            delivery = 150.0,
            total = 3250.0
        ),
        onIncrementar = {},
        onDecrementar = {},
        onNavigate = {}
    )
}
