package com.edu.ucne.parrilladalos2carnales.presentacion.confirmacion

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.tooling.preview.Preview
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import com.edu.ucne.parrilladalos2carnales.presentacion.componentes.AppTopBar

@Composable
fun ConfirmacionPedidoScreen(
    viewModel: ConfirmacionPedidoViewModel,
    onVolverInicio: () -> Unit,
    onVerEstado: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ConfirmacionPedidoContent(
        uiState = uiState,
        onVolverInicio = onVolverInicio,
        onVerEstado = onVerEstado
    )
}

@Composable
fun ConfirmacionPedidoContent(
    uiState: ConfirmacionPedidoUiState,
    onVolverInicio: () -> Unit,
    onVerEstado: (Int) -> Unit
) {
    val escala = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        escala.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Pedido Confirmado")
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(innerPadding), Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val pedido = uiState.pedido
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(110.dp)
                        .graphicsLayer {
                            scaleX = escala.value
                            scaleY = escala.value
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Pedido confirmado",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(70.dp)
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                Text(
                    text = "¡Gracias por tu pedido!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Orden:")
                            Text(pedido?.numeroOrden ?: "", fontWeight = FontWeight.Bold)
                        }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Tiempo estimado:")
                            Text(pedido?.tiempoEstimado ?: "", fontWeight = FontWeight.Bold)
                        }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Entrega:")
                            Text(pedido?.direccion ?: "", fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Total pagado:", style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "RD$ ${String.format("%.2f", pedido?.total ?: 0.0)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(Modifier.height(40.dp))

                Button(
                    onClick = onVolverInicio,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Volver al inicio", fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(12.dp))

                TextButton(
                    onClick = { pedido?.idPedido?.let { onVerEstado(it) } }
                ) {
                    Text("Ver estado del pedido", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmacionPedidoPreview() {
    ConfirmacionPedidoContent(
        uiState = ConfirmacionPedidoUiState(
            pedido = Pedido(
                idPedido = 1,
                total = 1500.0,
                direccion = "Calle 123",
                fecha = "20/05/2024"
            )
        ),
        onVolverInicio = {},
        onVerEstado = {}
    )
}
