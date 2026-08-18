package com.edu.ucne.parrilladalos2carnales.presentacion.menu.detalle

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import java.io.File

import androidx.compose.ui.tooling.preview.Preview
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlatoDetalleScreen(viewModel: PlatoDetalleViewModel, onBack: () -> Unit, onAgregadoAlCarrito: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.agregadoExitosamente) {
        if (uiState.agregadoExitosamente) {
            viewModel.onEvent(PlatoDetalleUiEvent.OnAgregarConsumido)
            onAgregadoAlCarrito()
        }
    }

    PlatoDetalleContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlatoDetalleContent(
    uiState: PlatoDetalleUiState,
    onEvent: (PlatoDetalleUiEvent) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).height(52.dp)) {
                    IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onSurface)
                    }
                    Text("Detalle", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.align(Alignment.Center))
                }
            }
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.background, tonalElevation = 0.dp) {
                Row(modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(10.dp, 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = MaterialTheme.colorScheme.outlineVariant, shape = RoundedCornerShape(28.dp), modifier = Modifier.height(44.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton({ onEvent(PlatoDetalleUiEvent.OnDecrementarCantidad) }, Modifier.size(40.dp)) { Icon(Icons.Default.Remove, "Disminuir") }
                            Text(uiState.cantidad.toString(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            IconButton({ onEvent(PlatoDetalleUiEvent.OnIncrementarCantidad) }, Modifier.size(40.dp)) { Icon(Icons.Default.Add, "Aumentar") }
                        }
                    }
                    Button({ onEvent(PlatoDetalleUiEvent.OnAgregarAlCarrito) }, shape = RoundedCornerShape(28.dp), contentPadding = PaddingValues(16.dp, 0.dp), modifier = Modifier.height(44.dp)) {
                        Icon(Icons.Default.Add, null)
                        Spacer(Modifier.width(5.dp))
                        Text("Añadir", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { p ->
        if (uiState.isLoading) Box(Modifier.fillMaxSize().padding(p), Alignment.Center) { CircularProgressIndicator() }
        else uiState.plato?.let { plato ->
            Column(Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState())) {
                Box(Modifier.padding(16.dp)) {
                    val model = remember(plato.imagenUrl) { if (plato.imagenUrl.startsWith("/")) File(plato.imagenUrl) else plato.imagenUrl.ifBlank { null } }
                    Card(shape = RoundedCornerShape(32.dp), elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier.fillMaxWidth().height(260.dp)) {
                        if (model != null) AsyncImage(model, plato.nombre, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        else Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) { Text("🥩", fontSize = 80.sp) }
                    }
                }
                Column(Modifier.padding(24.dp, 8.dp)) {
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.Top) {
                        Text(plato.nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
                        Text("RD$ ${plato.precio}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(plato.descripcion.ifBlank { "Acompañado con el sazón especial de D'Parrillada Los Dos Carnales." }, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 24.sp)
                    Spacer(Modifier.height(24.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(0.5f))
                    Spacer(Modifier.height(24.dp))
                    if (uiState.terminosCoccionDisponibles.isNotEmpty()) {
                        OptionSectionButtons("Término de la carne", uiState.terminosCoccionDisponibles, uiState.terminoSeleccionado?.idComponente, { onEvent(PlatoDetalleUiEvent.OnCoccionSelect(it as Componente)) }, { (it as Componente).nombreComponente.ifBlank { it.coccion ?: "" } })
                        Spacer(Modifier.height(24.dp))
                    }
                    if (uiState.guarnicionesDisponibles.isNotEmpty()) {
                        OptionSectionButtons("Elige tu Guarnición", uiState.guarnicionesDisponibles, uiState.guarnicionSeleccionada?.idGuarnicion, { onEvent(PlatoDetalleUiEvent.OnGuarnicionSelect(it as Guarnicion)) }, { (it as Guarnicion).nombreGuarnicion })
                        Spacer(Modifier.height(24.dp))
                    }
                    if (uiState.salsasDisponibles.isNotEmpty()) {
                        OptionSectionButtons("Salsa Extra", uiState.salsasDisponibles, uiState.salsaSeleccionada?.idComponente, { onEvent(PlatoDetalleUiEvent.OnSalsaSelect(it as Componente)) }, { (it as Componente).nombreComponente })
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlatoDetallePreview() {
    PlatoDetalleContent(
        uiState = PlatoDetalleUiState(
            plato = Plato(
                idPlato = 1,
                nombre = "Parrillada Mixta",
                precio = 1200.0,
                descripcion = "Una deliciosa parrillada con carnes premium."
            )
        ),
        onEvent = {},
        onBack = {}
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OptionSectionButtons(title: String, items: List<Any>, selectedId: Int?, onSelect: (Any) -> Unit, labelProvider: (Any) -> String) {
    Column(Modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(12.dp))
        FlowRow(Modifier.fillMaxWidth(), Arrangement.spacedBy(10.dp), Arrangement.spacedBy(10.dp)) {
            items.forEach { item ->
                val id = when (item) { is Componente -> item.idComponente; is Guarnicion -> item.idGuarnicion; else -> 0 }
                val isSelected = id == selectedId
                Surface(onClick = { onSelect(item) }, shape = RoundedCornerShape(50), color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.height(40.dp)) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(labelProvider(item), color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

