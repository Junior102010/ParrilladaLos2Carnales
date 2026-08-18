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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.presentacion.componentes.AppTopBar
import java.io.File

import androidx.compose.ui.tooling.preview.Preview
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlatoDetalleContent(
    uiState: PlatoDetalleUiState,
    onEvent: (PlatoDetalleUiEvent) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Detalle",
                onBack = onBack
            )
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

                    Column(horizontalAlignment = Alignment.End) {
                        if (uiState.precioExtrasUnitario > 0) {
                            Text(
                                text = "Extras: +RD$ ${"%.2f".format(uiState.precioExtrasUnitario * uiState.cantidad)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                        Button({ onEvent(PlatoDetalleUiEvent.OnAgregarAlCarrito) }, shape = RoundedCornerShape(28.dp), contentPadding = PaddingValues(16.dp, 0.dp), modifier = Modifier.height(44.dp)) {
                            Icon(Icons.Default.Add, null)
                            Spacer(Modifier.width(5.dp))
                            Text("Añadir", fontWeight = FontWeight.Bold)
                        }
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
                        
                        Column(horizontalAlignment = Alignment.End) {
                            if (uiState.ofertaActiva != null) {
                                val precioConOferta = plato.precio * (1.0 - uiState.ofertaActiva!!.descuento / 100.0)
                                Text(
                                    text = "RD$ ${"%.2f".format(plato.precio)}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textDecoration = TextDecoration.LineThrough
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "RD$ ${"%.2f".format(precioConOferta)}",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(4.dp)) {
                                        Text(
                                            text = "-${uiState.ofertaActiva!!.descuento.toInt()}%",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = "RD$ ${"%.2f".format(plato.precio)}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(plato.descripcion.ifBlank { "Acompañado con el sazón especial de D'Parrillada Los Dos Carnales." }, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 24.sp)
                    Spacer(Modifier.height(24.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(0.5f))
                    Spacer(Modifier.height(24.dp))

                    if (uiState.terminosCoccionDisponibles.isNotEmpty()) {
                        OptionSectionButtons(
                            title = "Término de la carne",
                            items = uiState.terminosCoccionDisponibles,
                            selectedId = uiState.terminoSeleccionado?.idComponente,
                            onSelect = { onEvent(PlatoDetalleUiEvent.OnCoccionSelect(it as Componente)) },
                            labelProvider = { (it as Componente).nombreComponente.ifBlank { it.coccion ?: "" } }
                        )
                        Spacer(Modifier.height(24.dp))
                    }

                    if (uiState.guarnicionesDisponibles.isNotEmpty()) {
                        OptionSectionButtons(
                            title = "Guarnición incluida",
                            items = uiState.guarnicionesDisponibles,
                            selectedId = uiState.guarnicionSeleccionada?.idGuarnicion,
                            onSelect = { onEvent(PlatoDetalleUiEvent.OnGuarnicionSelect(it as Guarnicion)) },
                            labelProvider = { (it as Guarnicion).nombreGuarnicion }
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "1 incluida con tu plato",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(Modifier.height(20.dp))

                        GuarnicionesExtraSection(
                            guarniciones = uiState.guarnicionesDisponibles,
                            incluida = uiState.guarnicionSeleccionada,
                            seleccionadas = uiState.guarnicionesExtraSeleccionadas,
                            onToggle = { onEvent(PlatoDetalleUiEvent.OnGuarnicionExtraToggle(it)) }
                        )
                        Spacer(Modifier.height(24.dp))
                    }

                    if (uiState.salsasDisponibles.isNotEmpty()) {
                        OptionSectionButtons(
                            title = "Salsa incluida",
                            items = uiState.salsasDisponibles,
                            selectedId = uiState.salsaSeleccionada?.idComponente,
                            onSelect = { onEvent(PlatoDetalleUiEvent.OnSalsaSelect(it as Componente)) },
                            labelProvider = { (it as Componente).nombreComponente }
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "1 incluida con tu plato",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(Modifier.height(20.dp))

                        SalsasExtraSection(
                            salsas = uiState.salsasDisponibles,
                            incluida = uiState.salsaSeleccionada,
                            seleccionadas = uiState.salsasExtraSeleccionadas,
                            onToggle = { onEvent(PlatoDetalleUiEvent.OnSalsaExtraToggle(it)) }
                        )
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

@Composable
fun GuarnicionesExtraSection(
    guarniciones: List<Guarnicion>,
    incluida: Guarnicion?,
    seleccionadas: List<Guarnicion>,
    onToggle: (Guarnicion) -> Unit
) {
    val extras = guarniciones.filter { it.idGuarnicion != incluida?.idGuarnicion }
    if (extras.isEmpty()) return

    Column(Modifier.fillMaxWidth()) {
        Text(
            text = "Guarniciones extra",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        extras.forEach { guarnicion ->
            val seleccionada = seleccionadas.any { it.idGuarnicion == guarnicion.idGuarnicion }
            
            Surface(
                onClick = { onToggle(guarnicion) },
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = seleccionada,
                        onCheckedChange = { onToggle(guarnicion) }
                    )
                    Text(
                        text = guarnicion.nombreGuarnicion,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "+ RD$ ${"%.2f".format(guarnicion.precioGuarnicion)}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun SalsasExtraSection(
    salsas: List<Componente>,
    incluida: Componente?,
    seleccionadas: List<Componente>,
    onToggle: (Componente) -> Unit
) {
    val extras = salsas.filter { it.idComponente != incluida?.idComponente }
    if (extras.isEmpty()) return

    Column(Modifier.fillMaxWidth()) {
        Text(
            text = "Salsas extra",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        extras.forEach { salsa ->
            val seleccionada = seleccionadas.any { it.idComponente == salsa.idComponente }

            Surface(
                onClick = { onToggle(salsa) },
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = seleccionada,
                        onCheckedChange = { onToggle(salsa) }
                    )
                    Text(
                        text = salsa.nombreComponente,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "+ RD$ ${"%.2f".format(salsa.precioComponente)}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
