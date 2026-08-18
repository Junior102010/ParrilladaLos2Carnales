package com.edu.ucne.parrilladalos2carnales.presentacion.perfil

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import com.edu.ucne.parrilladalos2carnales.ui.theme.ThemeManager
import com.edu.ucne.parrilladalos2carnales.ui.theme.ThemeMode
import java.io.File
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.edu.ucne.parrilladalos2carnales.presentacion.componentes.AppTopBar

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit,
    rolUsuario: Rol = Rol.CLIENTE
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val fotoModel = remember(uiState.fotoUrl) {
        val foto = uiState.fotoUrl
        when {
            foto.isNullOrBlank() -> null
            foto.startsWith("/") -> File(foto)
            else -> foto
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refrescarUsuario()
        viewModel.logoutEvent.collect {
            onLogout()
        }
    }

    PerfilContent(
        uiState = uiState,
        rolUsuario = rolUsuario,
        fotoModel = fotoModel,
        onNavigate = onNavigate,
        onLogout = { viewModel.cerrarSesion() },
        onToggleNotificaciones = { viewModel.setNotificaciones(it) }
    )
}

@Composable
fun PerfilContent(
    uiState: PerfilUiState,
    rolUsuario: Rol,
    fotoModel: Any?,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit,
    onToggleNotificaciones: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = if (rolUsuario == Rol.ADMINISTRADOR) "Perfil Administrador" else "Perfil y ajustes"
            )
        },
        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = if (rolUsuario == Rol.ADMINISTRADOR) Screen.AdminPerfil else Screen.Perfil,
                rolUsuario = rolUsuario,
                onNavigate = onNavigate
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { p ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(p)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    Arrangement.spacedBy(16.dp),
                    Alignment.CenterVertically
                ) {
                    if (fotoModel != null) {
                        AsyncImage(
                            model = fotoModel,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(45.dp)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = uiState.nombre,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = uiState.correo,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Surface(
                            shape = RoundedCornerShape(50),
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .primaryContainer
                        ) {

                            Text(
                                text =
                                    if (
                                        rolUsuario ==
                                        Rol.ADMINISTRADOR
                                    ) {
                                        "Administrador"
                                    } else {
                                        "Cliente"
                                    },

                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 5.dp
                                ),

                                style =
                                    MaterialTheme
                                        .typography
                                        .labelMedium,

                                fontWeight =
                                    FontWeight.Bold,

                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimaryContainer
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            onNavigate(
                                Screen.EditarPerfil(
                                    esAdministrador =
                                        rolUsuario == Rol.ADMINISTRADOR
                                )
                            )
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Edit,

                            contentDescription =
                                "Editar perfil",

                            tint =
                                MaterialTheme
                                    .colorScheme
                                    .primary
                        )
                    }
                }
            }

            if (rolUsuario == Rol.CLIENTE) {
                Card(
                    onClick = { onNavigate(Screen.Historial) },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.fillMaxWidth().padding(18.dp), Arrangement.spacedBy(14.dp), Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.ReceiptLong, null, tint = MaterialTheme.colorScheme.primary)
                        Column(Modifier.weight(1f)) {
                            Text("Mis pedidos", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text("Ver historial", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp), Arrangement.spacedBy(14.dp)) {
                    Row(Modifier, Arrangement.spacedBy(10.dp), Alignment.CenterVertically) {
                        Icon(Icons.Default.Brightness4, null, tint = MaterialTheme.colorScheme.primary)
                        Column {
                            Text("Apariencia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Elige cómo quieres ver la aplicación.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                        TemaCompactoItem("Sistema", ThemeManager.themeMode == ThemeMode.SISTEMA, Modifier.weight(1f)) { ThemeManager.themeMode = ThemeMode.SISTEMA }
                        TemaCompactoItem("Claro", ThemeManager.themeMode == ThemeMode.CLARO, Modifier.weight(1f)) { ThemeManager.themeMode = ThemeMode.CLARO }
                        TemaCompactoItem("Oscuro", ThemeManager.themeMode == ThemeMode.OSCURO, Modifier.weight(1f)) { ThemeManager.themeMode = ThemeMode.OSCURO }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    Arrangement.spacedBy(14.dp),
                    Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Notificaciones",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = if (rolUsuario == Rol.ADMINISTRADOR) {
                                "Avisos de nuevos pedidos y cambios importantes."
                            } else {
                                "Avisos de pedidos y nuevas ofertas."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = uiState.notificacionesActivas,
                        onCheckedChange = onToggleNotificaciones
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(26.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilPreview() {
    PerfilContent(
        uiState = PerfilUiState(
            nombre = "Cliente de Prueba",
            correo = "cliente@ejemplo.com"
        ),
        rolUsuario = Rol.CLIENTE,
        fotoModel = null,
        onNavigate = {},
        onLogout = {},
        onToggleNotificaciones = {}
    )
}

@Composable
private fun TemaCompactoItem(texto: String, seleccionado: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(22.dp),
        color = if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text(texto, style = MaterialTheme.typography.labelLarge, fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium)
        }
    }
}
