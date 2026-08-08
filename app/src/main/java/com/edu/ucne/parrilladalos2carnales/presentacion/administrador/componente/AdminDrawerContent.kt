package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsSystemDaydream
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import com.edu.ucne.parrilladalos2carnales.ui.theme.ThemeManager
import com.edu.ucne.parrilladalos2carnales.ui.theme.ThemeMode

private val WarmOrangePrimary = Color(0xFFF26522)

@Composable
fun AdminDrawerContent(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Encabezado del Menú Lateral con Naranja Cálido
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(WarmOrangePrimary)
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = "Parrillada Admin",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Panel de Administración",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(16.dp))

            // Selector de Apariencia / Tema de Color
            Text(
                text = "CAMBIAR TEMA DE COLOR",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = WarmOrangePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            NavigationDrawerItem(
                label = { Text("Modo Claro ☀️", fontWeight = FontWeight.SemiBold) },
                selected = ThemeManager.themeMode == ThemeMode.CLARO,
                onClick = {
                    ThemeManager.themeMode = ThemeMode.CLARO
                    onCloseDrawer()
                },
                icon = { Icon(Icons.Default.LightMode, contentDescription = null) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = WarmOrangePrimary.copy(alpha = 0.18f),
                    selectedIconColor = WarmOrangePrimary,
                    selectedTextColor = WarmOrangePrimary
                )
            )

            NavigationDrawerItem(
                label = { Text("Modo Oscuro 🌙", fontWeight = FontWeight.SemiBold) },
                selected = ThemeManager.themeMode == ThemeMode.OSCURO,
                onClick = {
                    ThemeManager.themeMode = ThemeMode.OSCURO
                    onCloseDrawer()
                },
                icon = { Icon(Icons.Default.DarkMode, contentDescription = null) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = WarmOrangePrimary.copy(alpha = 0.18f),
                    selectedIconColor = WarmOrangePrimary,
                    selectedTextColor = WarmOrangePrimary
                )
            )

            NavigationDrawerItem(
                label = { Text("Automático (Sistema) 📱", fontWeight = FontWeight.SemiBold) },
                selected = ThemeManager.themeMode == ThemeMode.SISTEMA,
                onClick = {
                    ThemeManager.themeMode = ThemeMode.SISTEMA
                    onCloseDrawer()
                },
                icon = { Icon(Icons.Default.SettingsSystemDaydream, contentDescription = null) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = WarmOrangePrimary.copy(alpha = 0.18f),
                    selectedIconColor = WarmOrangePrimary,
                    selectedTextColor = WarmOrangePrimary
                )
            )

            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Cerrar Sesión
            Button(
                onClick = {
                    onCloseDrawer()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar Sesión"
                    )
                    Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}