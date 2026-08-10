package com.edu.ucne.parrilladalos2carnales.presentacion.pago.funciones


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.TipoEntrega
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.MetodoPago
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.PagoUiState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.OutlinedTextFieldDefaults

@Composable
fun PagoTopBar(
    onBack: () -> Unit
) {

    Surface(
        color =
            MaterialTheme
                .colorScheme.surface,

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

            IconButton(
                onClick = onBack,

                modifier =
                    Modifier.align(
                        Alignment.CenterStart
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored
                            .Filled.ArrowBack,

                    contentDescription =
                        "Volver",

                    tint =
                        MaterialTheme
                            .colorScheme
                            .onSurface
                )
            }

            Text(
                text = "Pago",

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

                modifier =
                    Modifier.align(
                        Alignment.Center
                    )
            )
        }
    }
}



@Composable
fun DireccionCard(
    direccion: String
) {

    Surface(
        shape =
            RoundedCornerShape(30.dp),

        color =
            MaterialTheme
                .colorScheme
                .surface
                .copy(
                    alpha = 0.55f
                ),

        border =
            BorderStroke(
                2.dp,
                MaterialTheme
                    .colorScheme
                    .outline
                    .copy(
                        alpha = 0.45f
                    )
            ),

        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
    ) {

        Row(
            modifier =
                Modifier.padding(
                    20.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    Icons.Default.LocationOn,

                contentDescription =
                    null,

                tint =
                    MaterialTheme
                        .colorScheme
                        .onSurface
            )

            Spacer(
                Modifier.width(10.dp)
            )

            Text(
                text = direccion,

                style =
                    MaterialTheme
                        .typography
                        .titleMedium,

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface
            )
        }
    }
}

@Composable
fun TipoEntregaSelector(
    seleccionado: TipoEntrega,
    onSeleccionar:
        (TipoEntrega) -> Unit
) {

    Surface(
        shape =
            RoundedCornerShape(30.dp),

        color =
            MaterialTheme
                .colorScheme
                .surfaceVariant,

        border =
            BorderStroke(
                1.dp,
                MaterialTheme
                    .colorScheme
                    .outlineVariant
            ),

        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
    ) {

        Row(
            modifier =
                Modifier.fillMaxSize()
        ) {

            SelectorEntregaItem(
                texto = "Delivery",

                seleccionado =
                    seleccionado ==
                            TipoEntrega.DELIVERY,

                modifier =
                    Modifier.weight(1f),

                onClick = {
                    onSeleccionar(
                        TipoEntrega.DELIVERY
                    )
                }
            )

            SelectorEntregaItem(
                texto = "Recoger",

                seleccionado =
                    seleccionado ==
                            TipoEntrega.RECOGER,

                modifier =
                    Modifier.weight(1f),

                onClick = {
                    onSeleccionar(
                        TipoEntrega.RECOGER
                    )
                }
            )
        }
    }
}

@Composable
private fun SelectorEntregaItem(
    texto: String,
    seleccionado: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Surface(
        onClick = onClick,

        color =
            if (seleccionado) {
                MaterialTheme
                    .colorScheme.primary
            } else {
                MaterialTheme
                    .colorScheme
                    .surfaceVariant
            },

        contentColor =
            if (seleccionado) {
                MaterialTheme
                    .colorScheme.onPrimary
            } else {
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
            },

        shape =
            RoundedCornerShape(30.dp),

        modifier =
            modifier
                .fillMaxHeight()
                .padding(5.dp)
    ) {

        Box(
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = texto,

                style =
                    MaterialTheme
                        .typography
                        .titleMedium,

                fontWeight =
                    FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun MetodoPagoSelector(
    seleccionado: MetodoPago,
    onSeleccionar:
        (MetodoPago) -> Unit
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.spacedBy(
                10.dp
            )
    ) {

        MetodoPagoChip(
            texto = "Efectivo",

            seleccionado =
                seleccionado ==
                        MetodoPago.EFECTIVO,

            modifier =
                Modifier.weight(1f),

            onClick = {
                onSeleccionar(
                    MetodoPago.EFECTIVO
                )
            }
        )

        MetodoPagoChip(
            texto = "Tarjeta",

            seleccionado =
                seleccionado ==
                        MetodoPago.TARJETA,

            modifier =
                Modifier.weight(1f),

            onClick = {
                onSeleccionar(
                    MetodoPago.TARJETA
                )
            }
        )

        MetodoPagoChip(
            texto = "Transferencia",

            seleccionado =
                seleccionado ==
                        MetodoPago.TRANSFERENCIA,

            modifier =
                Modifier.weight(1.4f),

            onClick = {
                onSeleccionar(
                    MetodoPago.TRANSFERENCIA
                )
            }
        )
    }
}

@Composable
private fun MetodoPagoChip(
    texto: String,
    seleccionado: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Surface(
        onClick = onClick,

        modifier =
            modifier.height(46.dp),

        shape =
            RoundedCornerShape(30.dp),

        color =
            if (seleccionado) {
                MaterialTheme
                    .colorScheme.primary
            } else {
                MaterialTheme
                    .colorScheme
                    .surfaceVariant
            },

        border =
            BorderStroke(
                2.dp,
                MaterialTheme
                    .colorScheme
                    .outlineVariant
            ),

        contentColor =
            if (seleccionado) {
                MaterialTheme
                    .colorScheme.onPrimary
            } else {
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
            }
    ) {

        Box(
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = texto,
                fontWeight =
                    FontWeight.Medium
            )
        }
    }
}

@Composable
fun TarjetaFormulario(
    uiState: PagoUiState,
    onNumeroChange: (String) -> Unit,
    onFechaChange: (String) -> Unit,
    onCvvChange: (String) -> Unit
) {

    Surface(
        shape =
            RoundedCornerShape(30.dp),

        color =
            MaterialTheme
                .colorScheme.surface,

        border =
            BorderStroke(
                2.dp,
                MaterialTheme
                    .colorScheme
                    .outline
                    .copy(
                        alpha = 0.45f
                    )
            ),

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            Text(
                text = "Número",

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface,

                fontWeight =
                    FontWeight.Medium
            )

            Spacer(
                Modifier.height(5.dp)
            )

            OutlinedTextField(
                value =
                    uiState.numeroTarjeta,

                onValueChange =
                    onNumeroChange,

                placeholder = {
                    Text(
                        "4002 0000 0000 0000"
                    )
                },

                trailingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default
                                .CreditCard,

                        contentDescription =
                            null
                    )
                },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Number
                    ),

                singleLine = true,

                shape =
                    RoundedCornerShape(
                        28.dp
                    ),

                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                Modifier.height(12.dp)
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text = "Fecha"
                    )

                    OutlinedTextField(
                        value =
                            uiState
                                .fechaTarjeta,

                        onValueChange =
                            onFechaChange,

                        placeholder = {
                            Text("MM/YY")
                        },

                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType
                                        .Number
                            ),

                        singleLine = true,

                        shape =
                            RoundedCornerShape(
                                28.dp
                            ),

                        modifier =
                            Modifier.fillMaxWidth()
                    )
                }

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text = "CVV"
                    )

                    OutlinedTextField(
                        value =
                            uiState.cvv,

                        onValueChange =
                            onCvvChange,

                        placeholder = {
                            Text("123")
                        },

                        trailingIcon = {

                            Icon(
                                imageVector =
                                    Icons.Default
                                        .Lock,

                                contentDescription =
                                    null
                            )
                        },

                        visualTransformation =
                            PasswordVisualTransformation(),

                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType
                                        .NumberPassword
                            ),

                        singleLine = true,

                        shape =
                            RoundedCornerShape(
                                28.dp
                            ),

                        modifier =
                            Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(
                Modifier.height(10.dp)
            )

            Text(
                text =
                    "Los datos de tarjeta no se guardan.",

                style =
                    MaterialTheme
                        .typography
                        .bodySmall,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        }
    }
}

@Composable
fun TransferenciaFormulario(
    uiState: PagoUiState,
    onTitularChange: (String) -> Unit,
    onCuentaChange: (String) -> Unit,
    onBancoChange: (String) -> Unit
) {

    Surface(
        shape =
            RoundedCornerShape(30.dp),

        color =
            MaterialTheme
                .colorScheme
                .surface,

        border =
            BorderStroke(
                width = 2.dp,

                color =
                    MaterialTheme
                        .colorScheme
                        .outline
                        .copy(
                            alpha = 0.55f
                        )
            ),

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(
                    horizontal = 18.dp,
                    vertical = 20.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    16.dp
                )
        ) {

            Text(
                text =
                    "Pago por transferencia",

                style =
                    MaterialTheme
                        .typography
                        .titleLarge,

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface
            )

            PagoTransferenciaTextField(
                value =
                    uiState
                        .titularTransferencia,

                onValueChange =
                    onTitularChange,

                label =
                    "Titular"
            )

            PagoTransferenciaTextField(
                value =
                    uiState
                        .cuentaOrigen,

                onValueChange =
                    onCuentaChange,

                label =
                    "Cuenta de origen"
            )

            PagoTransferenciaTextField(
                value =
                    uiState.banco,

                onValueChange =
                    onBancoChange,

                label =
                    "Banco"
            )

            Text(
                text =
                    "Adjunta el comprobante de la transferencia.",

                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PagoTransferenciaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {

    OutlinedTextField(
        value = value,

        onValueChange =
            onValueChange,

        label = {
            Text(label)
        },

        singleLine = true,

        shape =
            RoundedCornerShape(
                28.dp
            ),

        colors =
            OutlinedTextFieldDefaults
                .colors(

                    focusedBorderColor =
                        MaterialTheme
                            .colorScheme
                            .primary,

                    unfocusedBorderColor =
                        MaterialTheme
                            .colorScheme
                            .outline,

                    focusedContainerColor =
                        MaterialTheme
                            .colorScheme
                            .surface,

                    unfocusedContainerColor =
                        MaterialTheme
                            .colorScheme
                            .surface
                ),

        modifier =
            Modifier
                .fillMaxWidth()
                .height(60.dp)
    )
}

@Composable
fun EfectivoFormulario(
    monto: String,
    total: Double,
    onMontoChange: (String) -> Unit
) {

    Surface(
        shape =
            RoundedCornerShape(30.dp),

        color =
            MaterialTheme
                .colorScheme.surface,

        border =
            BorderStroke(
                2.dp,
                MaterialTheme
                    .colorScheme.outline
            ),

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp)
        ) {

            Text(
                text =
                    "Pago en efectivo",

                style =
                    MaterialTheme
                        .typography
                        .titleLarge,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = monto,

                onValueChange =
                    onMontoChange,

                label = {
                    Text(
                        "Cantidad a recibir"
                    )
                },

                prefix = {
                    Text("RD$ ")
                },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),

                singleLine = true,

                shape =
                    RoundedCornerShape(
                        28.dp
                    ),

                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                Modifier.height(8.dp)
            )

            Text(
                text =
                    "Total: RD$ ${
                        String.format(
                            "%.2f",
                            total
                        )
                    }"
            )
        }
    }
}

@Composable
fun NuevaDireccionDialog(
    direccionActual: String,
    onDismiss: () -> Unit,
    onGuardar: (String) -> Unit
) {

    var direccion by
    remember {
        mutableStateOf(
            if (
                direccionActual ==
                "Dirección actual"
            ) {
                ""
            } else {
                direccionActual
            }
        )
    }

    AlertDialog(
        onDismissRequest =
            onDismiss,

        title = {
            Text(
                text =
                    "Nueva dirección"
            )
        },

        text = {

            OutlinedTextField(
                value =
                    direccion,

                onValueChange = {
                    direccion = it
                },

                label = {
                    Text(
                        "Dirección"
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default
                                .LocationOn,

                        contentDescription =
                            null
                    )
                },

                modifier =
                    Modifier.fillMaxWidth()
            )
        },

        confirmButton = {
            Button(
                onClick = {
                    if (direccion.isNotBlank()
                    ) {
                        onGuardar(direccion
                        )
                    }
                }
            ) {
                Text("Guardar")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun PagoBottomBar(
    subtotal: Double,
    delivery: Double,
    total: Double,
    onConfirmar: () -> Unit,
    enabled: Boolean = true
) {

    Surface(
        color =
            MaterialTheme.colorScheme.surface,

        shadowElevation = 10.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        if (delivery > 0.0) {
                            "Subtotal RD$ ${
                                String.format(
                                    "%.2f",
                                    subtotal
                                )
                            } + Delivery RD$ ${
                                String.format(
                                    "%.2f",
                                    delivery
                                )
                            }"
                        } else {
                            "Sin costo de delivery"
                        },

                    style =
                        MaterialTheme
                            .typography
                            .labelSmall,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )

                Text(
                    text = "Total",

                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )

                Text(
                    text =
                        "RD$ ${
                            String.format(
                                "%.2f",
                                total
                            )
                        }",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            Button(
                onClick =
                    onConfirmar,
                
                enabled = enabled,

                shape =
                    RoundedCornerShape(
                        28.dp
                    ),

                modifier =
                    Modifier.height(52.dp)
            ) {

                Text(
                    text =
                        "Confirmar pedido",

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}

