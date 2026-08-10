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
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones.PagoTopBar
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones.TarjetaFormulario
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones.TipoEntregaSelector
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones.TransferenciaFormulario

@Composable
fun PagoScreen(
    viewModel: PagoViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onPagoConfirmado: () -> Unit
) {
    val uiState by
    viewModel.uiState.collectAsStateWithLifecycle()
    var mostrarNuevaDireccion by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.datosPagoValidos
    ) {
        if (
            uiState.datosPagoValidos
        ) {
            viewModel.onEvent(PagoUiEvent.OnValidacionConsumida)
            onPagoConfirmado()
        }
    }
    Scaffold(
        containerColor =
            MaterialTheme.colorScheme.background,
        topBar = { PagoTopBar(onBack = onBack)},
        bottomBar = {
            PagoBottomBar(
                subtotal = uiState.subtotal,
                delivery = uiState.delivery,
                total = uiState.total,
                onConfirmar = {
                    viewModel.onEvent(PagoUiEvent.OnConfirmarPago)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 14.dp,
                    vertical = 20.dp
                )
        ) {
            TipoEntregaSelector(
                seleccionado =
                    uiState.tipoEntrega,

                onSeleccionar = {

                    viewModel.onEvent(
                        PagoUiEvent
                            .OnTipoEntregaChange(it)
                    )
                }
            )
            Spacer(Modifier.height(22.dp))
            if (
                uiState.tipoEntrega ==
                TipoEntrega.DELIVERY
            ) {
                Text(
                    text = "Dirección",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(8.dp))
                DireccionCard(direccion = uiState.direccion)
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { mostrarNuevaDireccion = true },
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text(
                        text = "Añadir nueva dirección",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(28.dp))
            }

            Text(
                text = "Método de pago",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(12.dp))

            MetodoPagoSelector(
                seleccionado = uiState.metodoPago,
                onSeleccionar = { viewModel.onEvent(PagoUiEvent.OnMetodoPagoChange(it))
                }
            )
            Spacer(Modifier.height(18.dp))

            when (
                uiState.metodoPago
            ) {
                MetodoPago.TARJETA -> {
                    TarjetaFormulario(
                        uiState = uiState,
                        onNumeroChange = {
                            viewModel.onEvent(PagoUiEvent.OnNumeroTarjetaChange(it))
                        },
                        onFechaChange = {
                            viewModel.onEvent(PagoUiEvent.OnFechaTarjetaChange(it))
                        },
                        onCvvChange = { viewModel.onEvent(PagoUiEvent.OnCvvChange(it))
                        }
                    )
                }
                MetodoPago.EFECTIVO -> {
                    EfectivoFormulario(
                        monto = uiState.montoRecibido,
                        total = uiState.total,
                        onMontoChange = { viewModel.onEvent(PagoUiEvent.OnMontoRecibidoChange(it))
                        }
                    )
                }
                MetodoPago.TRANSFERENCIA -> {

                    TransferenciaFormulario(uiState = uiState,
                        onTitularChange = {
                            viewModel.onEvent(PagoUiEvent.OnTitularTransferenciaChange(it))
                        },
                        onCuentaChange = { viewModel.onEvent(PagoUiEvent.OnCuentaOrigenChange(it))
                        },
                        onBancoChange = {
                            viewModel.onEvent(PagoUiEvent.OnBancoChange(it))
                        }
                    )
                }
            }
            if (
                uiState.errorMessage != null
            ) {
                Spacer(Modifier.height(14.dp))
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(20.dp))
        }
    }

    if (mostrarNuevaDireccion) {

        NuevaDireccionDialog(
            direccionActual = uiState.direccion,
            onDismiss = { mostrarNuevaDireccion = false
            },
            onGuardar = {
                viewModel.onEvent(PagoUiEvent.OnDireccionChange(it))
                mostrarNuevaDireccion = false
            }
        )
    }
}