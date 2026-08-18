package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminPerfil

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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

import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AdminPerfilScreen(
    viewModel: AdminPerfilViewModel,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refrescarUsuario()
        viewModel.logoutEvent.collect {
            onLogout()
        }
    }

    AdminPerfilContent(
        uiState = uiState,
        onNavigate = onNavigate,
        onLogout = { viewModel.cerrarSesion() }
    )
}

@Composable
fun AdminPerfilContent(
    uiState: AdminPerfilUiState,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 4.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Perfil Administrador",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = Screen.AdminPerfil,
                rolUsuario = Rol.ADMINISTRADOR,
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
                    if (!uiState.fotoUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = uiState.fotoUrl,
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
                    Column {
                        Text(uiState.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Text(uiState.correo, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
fun AdminPerfilPreview() {
    AdminPerfilContent(
        uiState = AdminPerfilUiState(
            nombre = "Administrador",
            correo = "admin@parrillada.com"
        ),
        onNavigate = {},
        onLogout = {}
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
