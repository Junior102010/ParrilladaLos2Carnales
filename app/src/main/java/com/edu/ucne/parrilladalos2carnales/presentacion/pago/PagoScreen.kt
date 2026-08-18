package com.edu.ucne.parrilladalos2carnales.presentacion.pago

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones.DireccionCard
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones.EfectivoFormulario
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones.MetodoPagoSelector
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones.NuevaDireccionDialog
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones.PagoBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones.TipoEntregaSelector
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones.TransferenciaFormulario
import androidx.compose.ui.tooling.preview.Preview
import com.edu.ucne.parrilladalos2carnales.presentacion.componentes.AppTopBar

@Composable
fun PagoScreen(
    viewModel: PagoViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onPedidoCreado: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.pedidoCreadoId) {
        uiState.pedidoCreadoId?.let { id ->
            viewModel.onEvent(PagoUiEvent.OnPedidoCreadoConsumido)
            onPedidoCreado(id)
        }
    }

    PagoContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}

@Composable
fun PagoContent(
    uiState: PagoUiState,
    onEvent: (PagoUiEvent) -> Unit,
    onBack: () -> Unit
) {
    var mostrarNuevaDireccion by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = "Pago",
                onBack = onBack
            )
        },
        bottomBar = {
            PagoBottomBar(
                subtotal = uiState.subtotal,
                delivery = uiState.delivery,
                total = uiState.total,
                onConfirmar = { onEvent(PagoUiEvent.OnConfirmarPago) },
                enabled = !uiState.isLoading
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 14.dp, vertical = 20.dp)
            ) {
                TipoEntregaSelector(
                    seleccionado = uiState.tipoEntrega,
                    onSeleccionar = { onEvent(PagoUiEvent.OnTipoEntregaChange(it)) }
                )
                Spacer(Modifier.height(22.dp))
                if (uiState.tipoEntrega == TipoEntrega.DELIVERY) {
                    Text(text = "Dirección", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(8.dp))
                    DireccionCard(direccion = uiState.direccion)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { mostrarNuevaDireccion = true }, shape = RoundedCornerShape(28.dp), modifier = Modifier.fillMaxWidth().height(50.dp)) {
                        Text(text = "Añadir nueva dirección", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.height(28.dp))
                }

                Text(text = "Método de pago", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.height(12.dp))

                MetodoPagoSelector(
                    seleccionado = uiState.metodoPago,
                    onSeleccionar = { onEvent(PagoUiEvent.OnMetodoPagoChange(it)) }
                )
                Spacer(Modifier.height(18.dp))

                when (uiState.metodoPago) {
                    MetodoPago.EFECTIVO -> {
                        EfectivoFormulario(
                            monto = uiState.montoRecibido,
                            total = uiState.total,
                            onMontoChange = {
                                onEvent(PagoUiEvent.OnMontoRecibidoChange(it))
                            }
                        )
                    }
                    MetodoPago.TRANSFERENCIA -> {
                        TransferenciaFormulario(
                            uiState = uiState,
                            onTitularChange = {
                                onEvent(PagoUiEvent.OnTitularTransferenciaChange(it))
                            },
                            onBancoChange = {
                                onEvent(PagoUiEvent.OnBancoChange(it))
                            },
                            onReferenciaChange = {
                                onEvent(PagoUiEvent.OnReferenciaTransferenciaChange(it))
                            }
                        )
                    }
                }

                if (uiState.errorMessage != null) {
                    Spacer(Modifier.height(14.dp))
                    Text(text = uiState.errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }

    if (mostrarNuevaDireccion) {
        NuevaDireccionDialog(
            direccionActual = uiState.direccion,
            onDismiss = { mostrarNuevaDireccion = false },
            onGuardar = {
                onEvent(PagoUiEvent.OnDireccionChange(it))
                mostrarNuevaDireccion = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PagoPreview() {
    PagoContent(
        uiState = PagoUiState(
            subtotal = 3100.0,
            delivery = 150.0,
            total = 3250.0,
            tipoEntrega = TipoEntrega.DELIVERY,
            metodoPago = MetodoPago.EFECTIVO
        ),
        onEvent = {},
        onBack = {}
    )
}
