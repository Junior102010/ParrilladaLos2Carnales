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

import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onBack: () -> Unit,
    onRegisterSuccess: (Rol) -> Unit
) {
    RegisterContent(
        pasoActual = viewModel.pasoActual,
        estaCargando = viewModel.estaCargando,
        mensajeError = viewModel.mensajeError,
        nombreUsuario = viewModel.nombreUsuario,
        correo = viewModel.correo,
        contrasena = viewModel.contrasena,
        confirmarContrasena = viewModel.confirmarContrasena,
        contrasenaVisible = viewModel.contrasenaVisible,
        nombre = viewModel.nombre,
        apellido = viewModel.apellido,
        telefono = viewModel.telefono,
        calle = viewModel.calle,
        numero = viewModel.numero,
        ciudad = viewModel.ciudad,
        codigoPostal = viewModel.codigoPostal,
        referencia = viewModel.referencia,
        onNombreUsuarioChange = { viewModel.nombreUsuario = it },
        onCorreoChange = { viewModel.correo = it },
        onContrasenaChange = { viewModel.contrasena = it },
        onConfirmarContrasenaChange = { viewModel.confirmarContrasena = it },
        onAlternarVisibilidadContrasena = { viewModel.alternarVisibilidadContrasena() },
        onNombreChange = { viewModel.nombre = it },
        onApellidoChange = { viewModel.apellido = it },
        onTelefonoChange = { viewModel.telefono = it },
        onCalleChange = { viewModel.calle = it },
        onNumeroChange = { viewModel.numero = it },
        onCiudadChange = { viewModel.ciudad = it },
        onCodigoPostalChange = { viewModel.codigoPostal = it },
        onReferenciaChange = { viewModel.referencia = it },
        onSiguientePaso = { viewModel.siguientePaso() },
        onPasoAnterior = { viewModel.pasoAnterior() },
        onRegistrarse = { viewModel.registrarse(onRegisterSuccess) },
        onBack = onBack
    )
}

@Composable
fun RegisterContent(
    pasoActual: Int,
    estaCargando: Boolean,
    mensajeError: String?,
    nombreUsuario: String,
    correo: String,
    contrasena: String,
    confirmarContrasena: String,
    contrasenaVisible: Boolean,
    nombre: String,
    apellido: String,
    telefono: String,
    calle: String,
    numero: String,
    ciudad: String,
    codigoPostal: String,
    referencia: String,
    onNombreUsuarioChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onConfirmarContrasenaChange: (String) -> Unit,
    onAlternarVisibilidadContrasena: () -> Unit,
    onNombreChange: (String) -> Unit,
    onApellidoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onCalleChange: (String) -> Unit,
    onNumeroChange: (String) -> Unit,
    onCiudadChange: (String) -> Unit,
    onCodigoPostalChange: (String) -> Unit,
    onReferenciaChange: (String) -> Unit,
    onSiguientePaso: () -> Unit,
    onPasoAnterior: () -> Unit,
    onRegistrarse: () -> Unit,
    onBack: () -> Unit
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
                                if (pasoActual > index) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f),
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (pasoActual) {
                1 -> StepOneContent(nombreUsuario, correo, contrasena, confirmarContrasena, contrasenaVisible, onNombreUsuarioChange, onCorreoChange, onContrasenaChange, onConfirmarContrasenaChange, onAlternarVisibilidadContrasena)
                2 -> StepTwoContent(nombre, apellido, telefono, onNombreChange, onApellidoChange, onTelefonoChange)
                3 -> StepThreeContent(calle, numero, ciudad, codigoPostal, referencia, onCalleChange, onNumeroChange, onCiudadChange, onCodigoPostalChange, onReferenciaChange)
            }

            mensajeError?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { if (pasoActual > 1) onPasoAnterior() else onBack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = !estaCargando,
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
                        if (pasoActual < 3) {
                            onSiguientePaso()
                        } else {
                            onRegistrarse()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = !estaCargando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    if (estaCargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (pasoActual < 3) "Siguiente" else "Crear Cuenta",
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
private fun StepOneContent(
    nombreUsuario: String,
    correo: String,
    contrasena: String,
    confirmarContrasena: String,
    contrasenaVisible: Boolean,
    onNombreUsuarioChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onConfirmarContrasenaChange: (String) -> Unit,
    onAlternarVisibilidadContrasena: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RegisterTextField(value = nombreUsuario, onValueChange = onNombreUsuarioChange, placeholder = "Nombre de Usuario")
        RegisterTextField(value = correo, onValueChange = onCorreoChange, placeholder = "Correo Electrónico", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        RegisterTextField(
            value = contrasena,
            onValueChange = onContrasenaChange,
            placeholder = "Contraseña",
            visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (contrasenaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = onAlternarVisibilidadContrasena) {
                    Icon(icon, contentDescription = null)
                }
            }
        )
        RegisterTextField(value = confirmarContrasena, onValueChange = onConfirmarContrasenaChange, placeholder = "Confirmar Contraseña", visualTransformation = PasswordVisualTransformation())
    }
}

@Composable
private fun StepTwoContent(
    nombre: String,
    apellido: String,
    telefono: String,
    onNombreChange: (String) -> Unit,
    onApellidoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RegisterTextField(value = nombre, onValueChange = onNombreChange, placeholder = "Nombre")
        RegisterTextField(value = apellido, onValueChange = onApellidoChange, placeholder = "Apellido")
        RegisterTextField(value = telefono, onValueChange = onTelefonoChange, placeholder = "Teléfono", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
    }
}

@Composable
private fun StepThreeContent(
    calle: String,
    numero: String,
    ciudad: String,
    codigoPostal: String,
    referencia: String,
    onCalleChange: (String) -> Unit,
    onNumeroChange: (String) -> Unit,
    onCiudadChange: (String) -> Unit,
    onCodigoPostalChange: (String) -> Unit,
    onReferenciaChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RegisterTextField(value = calle, onValueChange = onCalleChange, placeholder = "Calle")
        RegisterTextField(value = numero, onValueChange = onNumeroChange, placeholder = "Número")
        RegisterTextField(value = ciudad, onValueChange = onCiudadChange, placeholder = "Ciudad")
        RegisterTextField(value = codigoPostal, onValueChange = onCodigoPostalChange, placeholder = "Código Postal", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        RegisterTextField(value = referencia, onValueChange = onReferenciaChange, placeholder = "Referencia")
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterContent(
        pasoActual = 1,
        estaCargando = false,
        mensajeError = null,
        nombreUsuario = "",
        correo = "",
        contrasena = "",
        confirmarContrasena = "",
        contrasenaVisible = false,
        nombre = "",
        apellido = "",
        telefono = "",
        calle = "",
        numero = "",
        ciudad = "",
        codigoPostal = "",
        referencia = "",
        onNombreUsuarioChange = {},
        onCorreoChange = {},
        onContrasenaChange = {},
        onConfirmarContrasenaChange = {},
        onAlternarVisibilidadContrasena = {},
        onNombreChange = {},
        onApellidoChange = {},
        onTelefonoChange = {},
        onCalleChange = {},
        onNumeroChange = {},
        onCiudadChange = {},
        onCodigoPostalChange = {},
        onReferenciaChange = {},
        onSiguientePaso = {},
        onPasoAnterior = {},
        onRegistrarse = {},
        onBack = {}
    )
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
