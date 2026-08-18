package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.oferta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminSearchField
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminTopBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOfertaScreen(
    viewModel: AdminOfertaViewModel,
    onBack: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AdminTopBar(
                title = "Ofertas Especiales",
                onBack = onBack,
                compactTitle = true
            )
        },
        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = Screen.AdminPlatoList,
                rolUsuario = Rol.ADMINISTRADOR,
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.iniciarNuevaOferta() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Oferta")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AdminSearchField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = "Buscar oferta o plato...",
                modifier = Modifier.padding(16.dp)
            )

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.ofertas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay ofertas configuradas", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.ofertasFiltradas) { oferta ->
                        AdminOfertaCard(
                            oferta = oferta,
                            plato = uiState.platos.find { it.idPlato == oferta.idPlato },
                            onEdit = { viewModel.iniciarEdicionOferta(oferta) },
                            onDelete = { viewModel.eliminarOferta(oferta) },
                            onToggle = { activa ->
                                viewModel.onActivaChanged(activa)
                                viewModel.iniciarEdicionOferta(oferta.copy(activa = activa))
                                viewModel.guardarOferta()
                            }
                        )
                    }
                }
            }
        }
    }

    if (uiState.editorVisible) {
        OfertaEditorDialog(
            uiState = uiState,
            onDismiss = { viewModel.onEditorVisibleChanged(false) },
            onSave = { viewModel.guardarOferta() },
            onTituloChanged = { viewModel.onTituloChanged(it) },
            onDescripcionChanged = { viewModel.onDescripcionChanged(it) },
            onDescuentoChanged = { viewModel.onDescuentoChanged(it) },
            onPlatoSelected = { viewModel.onPlatoSelected(it) },
            onActivaChanged = { viewModel.onActivaChanged(it) }
        )
    }
}

@Composable
fun AdminOfertaCard(
    oferta: Oferta,
    plato: Plato?,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggle: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = if (oferta.activa) 1f else 0.6f
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = oferta.tituloOferta,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (plato != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = plato.nombre,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.width(7.dp))

                        Text(
                            text = "${oferta.descuento.toInt()}% de descuento",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                com.edu.ucne.parrilladalos2carnales.presentacion.administrador.AdminAvailabilitySwitch(
                    checked = oferta.activa,
                    onCheckedChange = onToggle
                )
            }

            if (oferta.descripcionOferta.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = oferta.descripcionOferta,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfertaEditorDialog(
    uiState: AdminOfertaUiState,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onTituloChanged: (String) -> Unit,
    onDescripcionChanged: (String) -> Unit,
    onDescuentoChanged: (String) -> Unit,
    onPlatoSelected: (Int?) -> Unit,
    onActivaChanged: (Boolean) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (uiState.idOfertaEditando == 0) "Nueva Oferta" else "Editar Oferta",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = uiState.tituloOferta,
                    onValueChange = onTituloChanged,
                    label = { Text("Título de la oferta") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                OutlinedTextField(
                    value = uiState.descripcionOferta,
                    onValueChange = onDescripcionChanged,
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    minLines = 2
                )

                OutlinedTextField(
                    value = uiState.descuento,
                    onValueChange = onDescuentoChanged,
                    label = { Text("Porcentaje de descuento (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Text("Plato al que aplica", style = MaterialTheme.typography.labelMedium)
                PlatoSelector(
                    platos = uiState.platos,
                    idPlatoSeleccionado = uiState.idPlatoSeleccionado,
                    onPlatoSelected = onPlatoSelected
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Oferta activa")
                    Switch(checked = uiState.activa, onCheckedChange = onActivaChanged)
                }

                if (uiState.errorMessage != null) {
                    Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = onSave,
                        enabled = !uiState.isSaving,
                        shape = RoundedCornerShape(50)
                    ) {
                        if (uiState.isSaving) CircularProgressIndicator(Modifier.size(20.dp), color = Color.White)
                        else Text("Guardar")
                    }
                }
            }
        }
    }
}

@Composable
fun PlatoSelector(
    platos: List<Plato>,
    idPlatoSeleccionado: Int?,
    onPlatoSelected: (Int?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val seleccionado = platos.find { it.idPlato == idPlatoSeleccionado }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = seleccionado?.nombre ?: "Seleccionar plato",
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Ninguno (Aplica a todo - informativo)") },
                onClick = { onPlatoSelected(null); expanded = false }
            )
            platos.forEach { plato ->
                DropdownMenuItem(
                    text = { Text(plato.nombre) },
                    onClick = { onPlatoSelected(plato.idPlato); expanded = false }
                )
            }
        }
    }
}
