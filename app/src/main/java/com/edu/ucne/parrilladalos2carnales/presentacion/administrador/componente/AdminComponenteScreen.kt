package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminComponenteScreen(
    viewModel: AdminComponenteViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.guardadoExitoso) {
        if (uiState.guardadoExitoso) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Complemento", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(AdminComponenteUiEvent.OnGuardarClick) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Guardar", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Selector de Tipo de Componente
            Text("Tipo de Complemento", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = uiState.categoriaComponente == "Salsa",
                    onClick = { viewModel.onEvent(AdminComponenteUiEvent.OnCategoriaChange("Salsa")) },
                    label = { Text("Salsa") }
                )
                FilterChip(
                    selected = uiState.categoriaComponente == "Coccion",
                    onClick = { viewModel.onEvent(AdminComponenteUiEvent.OnCategoriaChange("Coccion")) },
                    label = { Text("Término de Cocción") }
                )
            }

            OutlinedTextField(
                value = uiState.nombreComponente,
                onValueChange = { viewModel.onEvent(AdminComponenteUiEvent.OnNombreChange(it)) },
                label = { Text("Nombre (Ej. BBQ, Término Medio)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Solo mostrar precio si es Salsa (normalmente los términos no cuestan extra, pero lo dejamos editable por si acaso)
            if (uiState.categoriaComponente == "Salsa") {
                OutlinedTextField(
                    value = uiState.precioComponente,
                    onValueChange = { viewModel.onEvent(AdminComponenteUiEvent.OnPrecioChange(it)) },
                    label = { Text("Precio Adicional (RD$)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Disponible",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Switch(
                    checked = uiState.disponible,
                    onCheckedChange = { viewModel.onEvent(AdminComponenteUiEvent.OnDisponibleChange(it)) }
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}