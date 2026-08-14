package com.edu.ucne.parrilladalos2carnales.presentacion.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onBack: () -> Unit,
    onRegisterSuccess: (Rol) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .background(
                                if (viewModel.pasoActual > index) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f),
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (viewModel.pasoActual) {
                1 -> StepOne(viewModel)
                2 -> StepTwo(viewModel)
                3 -> StepThree(viewModel)
            }

            viewModel.mensajeError?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { if (viewModel.pasoActual > 1) viewModel.pasoAnterior() else onBack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = !viewModel.estaCargando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Atrás",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = {
                        if (viewModel.pasoActual < 3) {
                            viewModel.siguientePaso()
                        } else {
                            viewModel.registrarse(onRegisterSuccess)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = !viewModel.estaCargando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    if (viewModel.estaCargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (viewModel.pasoActual < 3) "Siguiente" else "Crear Cuenta",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepOne(viewModel: RegisterViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RegisterTextField(value = viewModel.nombreUsuario, onValueChange = { viewModel.nombreUsuario = it }, placeholder = "Nombre de Usuario")
        RegisterTextField(value = viewModel.correo, onValueChange = { viewModel.correo = it }, placeholder = "Correo Electrónico", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        RegisterTextField(
            value = viewModel.contrasena,
            onValueChange = { viewModel.contrasena = it },
            placeholder = "Contraseña",
            visualTransformation = if (viewModel.contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (viewModel.contrasenaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { viewModel.alternarVisibilidadContrasena() }) {
                    Icon(icon, contentDescription = null)
                }
            }
        )
        RegisterTextField(value = viewModel.confirmarContrasena, onValueChange = { viewModel.confirmarContrasena = it }, placeholder = "Confirmar Contraseña", visualTransformation = PasswordVisualTransformation())
    }
}

@Composable
private fun StepTwo(viewModel: RegisterViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RegisterTextField(value = viewModel.nombre, onValueChange = { viewModel.nombre = it }, placeholder = "Nombre")
        RegisterTextField(value = viewModel.apellido, onValueChange = { viewModel.apellido = it }, placeholder = "Apellido")
        RegisterTextField(value = viewModel.telefono, onValueChange = { viewModel.telefono = it }, placeholder = "Teléfono", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
    }
}

@Composable
private fun StepThree(viewModel: RegisterViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RegisterTextField(value = viewModel.calle, onValueChange = { viewModel.calle = it }, placeholder = "Calle")
        RegisterTextField(value = viewModel.numero, onValueChange = { viewModel.numero = it }, placeholder = "Número")
        RegisterTextField(value = viewModel.ciudad, onValueChange = { viewModel.ciudad = it }, placeholder = "Ciudad")
        RegisterTextField(value = viewModel.codigoPostal, onValueChange = { viewModel.codigoPostal = it }, placeholder = "Código Postal", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        RegisterTextField(value = viewModel.referencia, onValueChange = { viewModel.referencia = it }, placeholder = "Referencia")
    }
}

@Composable
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        ),
        singleLine = true
    )
}
