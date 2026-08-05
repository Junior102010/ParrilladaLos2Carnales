package com.edu.ucne.parrilladalos2carnales.presentacion.menu.detalle

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatoDetalleScreen(
    viewModel: PlatoDetalleViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.agregadoExitosamente) {
        if (uiState.agregadoExitosamente) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            // CORRECCIÓN: Restaurado a surfaceVariant para mantener el color oscuro/gris superior
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Detalle",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Acción de Perfil */ }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Perfil",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { /* Menú de opciones */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Más opciones",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            IconButton(onClick = { viewModel.onEvent(PlatoDetalleUiEvent.OnDecrementarCantidad) }) {
                                Icon(Icons.Default.Remove, contentDescription = "Menos")
                            }
                            Text(
                                text = "${uiState.cantidad}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(horizontal = 12.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(onClick = { viewModel.onEvent(PlatoDetalleUiEvent.OnIncrementarCantidad) }) {
                                Icon(Icons.Default.Add, contentDescription = "Más")
                            }
                        }
                    }

                    Button(
                        onClick = { viewModel.onEvent(PlatoDetalleUiEvent.OnAgregarAlCarrito) },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .height(56.dp)
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "Añadir • RD$ ${String.format("%.2f", uiState.precioTotal)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val plato = uiState.plato

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Tarjeta de Imagen
                Box(modifier = Modifier.padding(16.dp)) {
                    val imageModel = remember(plato?.imagenUrl) {
                        plato?.imagenUrl?.let { if (it.startsWith("/")) File(it) else it.ifBlank { null } }
                    }

                    Card(
                        shape = RoundedCornerShape(32.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    ) {
                        if (imageModel != null) {
                            AsyncImage(
                                model = imageModel,
                                contentDescription = plato?.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🥩", fontSize = 80.sp)
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = plato?.nombre ?: "Nombre del Platillo",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "RD$ ${plato?.precio ?: "0.00"}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = plato?.descripcion.takeIf { !it.isNullOrBlank() }
                            ?: "Acompañado con el sazón especial de D'Parrillada Los Dos Carnales.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(24.dp))

                    if (uiState.terminosCoccionDisponibles.isNotEmpty()) {
                        OptionSectionButtons(
                            title = "Término de la carne",
                            items = uiState.terminosCoccionDisponibles,
                            selectedId = uiState.terminoSeleccionado?.idComponente,
                            onSelect = { viewModel.onEvent(PlatoDetalleUiEvent.OnCoccionSelect(it as Componente)) },
                            labelProvider = { (it as Componente).nombreComponente.ifBlank { it.coccion ?: "" } }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    if (uiState.guarnicionesDisponibles.isNotEmpty()) {
                        OptionSectionButtons(
                            title = "Elige tu Guarnición",
                            items = uiState.guarnicionesDisponibles,
                            selectedId = uiState.guarnicionSeleccionada?.idGuarnicion,
                            onSelect = { viewModel.onEvent(PlatoDetalleUiEvent.OnGuarnicionSelect(it as Guarnicion)) },
                            labelProvider = { (it as Guarnicion).nombreGuarnicion }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    if (uiState.salsasDisponibles.isNotEmpty()) {
                        OptionSectionButtons(
                            title = "Salsa Extra",
                            items = uiState.salsasDisponibles,
                            selectedId = uiState.salsaSeleccionada?.idComponente,
                            onSelect = { viewModel.onEvent(PlatoDetalleUiEvent.OnSalsaSelect(it as Componente)) },
                            labelProvider = { (it as Componente).nombreComponente }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OptionSectionButtons(
    title: String,
    items: List<Any>,
    selectedId: Int?,
    onSelect: (Any) -> Unit,
    labelProvider: (Any) -> String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items.forEach { item ->
                val id = when (item) {
                    is Componente -> item.idComponente
                    is Guarnicion -> item.idGuarnicion
                    else -> 0
                }
                val isSelected = id == selectedId

                Surface(
                    onClick = { onSelect(item) },
                    shape = RoundedCornerShape(50),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.height(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = labelProvider(item),
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}