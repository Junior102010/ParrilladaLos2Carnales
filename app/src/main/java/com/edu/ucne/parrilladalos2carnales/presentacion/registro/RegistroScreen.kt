package com.edu.ucne.parrilladalos2carnales.presentacion.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(onNavigateBack: () -> Unit) {

    var currentStep by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { if (currentStep > 1) currentStep-- else onNavigateBack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
                Text(
                    text = "Crear Cuenta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProgressBarItem(isActive = currentStep >= 1, modifier = Modifier.weight(1f))
            ProgressBarItem(isActive = currentStep >= 2, modifier = Modifier.weight(1f))
            ProgressBarItem(isActive = currentStep >= 3, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Paso $currentStep de 3", color = Color(0xFFF6F6F6))

        Spacer(modifier = Modifier.height(16.dp))


        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Icono",
                        tint = Color(0xFF2C3E50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (currentStep) {
                            1 -> "Datos Personales"
                            2 -> "Dirección"
                            else -> "Contraseña"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))


                when (currentStep) {
                    1 -> PasoDatosPersonales()
                    2 -> { /* TODO: Paso Dirección */ }
                    3 -> { /* TODO: Paso Contraseña */ }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentStep > 1) {
                Button(
                    onClick = { currentStep-- },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6C8C3)),
                    modifier = Modifier.height(50.dp).weight(1f)
                ) {
                    Text("Atrás", color = Color(0xFF2C3E50), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
            }

            Button(
                onClick = { if (currentStep < 3) currentStep++ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF944927)),
                modifier = Modifier.height(50.dp).weight(if (currentStep > 1) 1f else 0.5f)
            ) {
                Text(if (currentStep == 3) "Crear Cuenta" else "Siguiente", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }


        if (currentStep == 1) {
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
fun ProgressBarItem(isActive: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(4.dp)
            .background(
                color = if (isActive) Color(0xFF944927) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(2.dp)
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasoDatosPersonales() {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFF6F6F6),
        focusedContainerColor = Color(0xFFF6F6F6),
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = Color(0xFF944927)
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = nombre, onValueChange = { nombre = it },
            placeholder = { Text("Nombre", color = Color.Gray, fontWeight = FontWeight.Bold) },
            singleLine = true, shape = RoundedCornerShape(24.dp), colors = textFieldColors, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = apellido, onValueChange = { apellido = it },
            placeholder = { Text("Apellido", color = Color.Gray, fontWeight = FontWeight.Bold) },
            singleLine = true, shape = RoundedCornerShape(24.dp), colors = textFieldColors, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = telefono, onValueChange = { telefono = it },
            placeholder = { Text("Teléfono/Celular", color = Color.Gray, fontWeight = FontWeight.Bold) },
            singleLine = true, shape = RoundedCornerShape(24.dp), colors = textFieldColors, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = correo, onValueChange = { correo = it },
            placeholder = { Text("Correo Electrónico", color = Color.Gray, fontWeight = FontWeight.Bold) },
            singleLine = true, shape = RoundedCornerShape(24.dp), colors = textFieldColors, modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasoDireccion() {
    var municipio by remember { mutableStateOf("") }
    var sector by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var numeroApto by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFF6F6F6),
        focusedContainerColor = Color(0xFFF6F6F6),
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = Color(0xFF944927)
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        OutlinedTextField(
            value = municipio, onValueChange = { municipio = it },
            placeholder = { Text("Municipio", color = Color.Gray, fontWeight = FontWeight.Bold) },
            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Desplegar", tint = Color.Gray) },
            singleLine = true, shape = RoundedCornerShape(24.dp), colors = textFieldColors, modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = sector, onValueChange = { sector = it },
            placeholder = { Text("Sector", color = Color.Gray, fontWeight = FontWeight.Bold) },
            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Desplegar", tint = Color.Gray) },
            singleLine = true, shape = RoundedCornerShape(24.dp), colors = textFieldColors, modifier = Modifier.fillMaxWidth()
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = calle, onValueChange = { calle = it },
                placeholder = { Text("Calle", color = Color.Gray, fontWeight = FontWeight.Bold) },
                singleLine = true, shape = RoundedCornerShape(24.dp), colors = textFieldColors,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = numeroApto, onValueChange = { numeroApto = it },
                placeholder = { Text("NO. / Apto", color = Color.Gray, fontWeight = FontWeight.Bold) },
                singleLine = true, shape = RoundedCornerShape(24.dp), colors = textFieldColors,
                modifier = Modifier.weight(1f)
            )
        }


        OutlinedTextField(
            value = referencia, onValueChange = { referencia = it },
            placeholder = { Text("Referencia", color = Color.Gray, fontWeight = FontWeight.Bold) },
            shape = RoundedCornerShape(24.dp), colors = textFieldColors,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasoContrasena() {
    var contrasena by remember { mutableStateOf("") }
    var confirmacion by remember { mutableStateOf("") }


    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFF6F6F6),
        focusedContainerColor = Color(0xFFF6F6F6),
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = Color(0xFF944927)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = contrasena, onValueChange = { contrasena = it },
            placeholder = { Text("Contraseña", color = Color.Gray, fontWeight = FontWeight.Bold) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true, shape = RoundedCornerShape(24.dp), colors = textFieldColors, modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = confirmacion, onValueChange = { confirmacion = it },
            placeholder = { Text("Confirmación", color = Color.Gray, fontWeight = FontWeight.Bold) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true, shape = RoundedCornerShape(24.dp), colors = textFieldColors, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        Text(
            text = "Al crear una cuenta, aceptas nuestros Términos y Condiciones.",
            color = Color.Gray,
            fontSize = 13.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}