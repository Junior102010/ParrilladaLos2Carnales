package com.edu.ucne.parrilladalos2carnales.presentacion.login

import com.edu.ucne.parrilladalos2carnales.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    // Estados para guardar lo que el usuario escribe
    var usuarioText by remember { mutableStateOf("") }
    var contrasenaText by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Logo
        Image(
            painter = painterResource(id = R.drawable.parrillada_sin_fondo),
            contentDescription = "Logo D'Parrillada Los Dos Carnales",
            modifier = Modifier.size(220.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 2. Campo Usuario
        OutlinedTextField(
            value = usuarioText,
            onValueChange = { usuarioText = it },
            placeholder = { Text("Usuario", color = Color.Gray, fontWeight = FontWeight.Bold) },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF6F6F6),
                focusedContainerColor = Color(0xFFF6F6F6),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF944927),
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = contrasenaText,
            onValueChange = { contrasenaText = it },
            placeholder = { Text("Contraseña", color = Color.Gray, fontWeight = FontWeight.Bold) },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),

            trailingIcon = {
                TextButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Text(
                        text = if (isPasswordVisible) "Ocultar" else "Ver",
                        color = Color.Gray
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF6F6F6),
                focusedContainerColor = Color(0xFFF6F6F6),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF944927),
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))


        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF944927)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp)
        ) {
            Text("Iniciar sesión", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))


        TextButton(onClick = {  }) {
            Text(
                text = "No Tienes una Cuenta? Registrate!",
                color = Color(0xFFE9AE3C),
                fontWeight = FontWeight.Bold
            )
        }
    }
}