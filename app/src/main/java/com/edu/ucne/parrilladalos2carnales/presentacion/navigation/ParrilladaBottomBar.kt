package com.edu.ucne.parrilladalos2carnales.presentacion.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol

@Composable
fun ParrilladaBottomBar(
    currentScreen: Screen,
    rolUsuario: Rol,
    onNavigate: (Screen) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (rolUsuario == Rol.ADMINISTRADOR) {
                BottomNavItem(
                    icon = Icons.Default.RestaurantMenu,
                    isSelected = currentScreen is Screen.AdminPlatoList,
                    onClick = { onNavigate(Screen.AdminPlatoList) }
                )
                BottomNavItem(
                    icon = Icons.Default.Settings,
                    isSelected = currentScreen is Screen.AdminPlatoEntry,
                    onClick = { }
                )
            } else {
                BottomNavItem(
                    icon = Icons.Default.Home,
                    isSelected = currentScreen is Screen.Inicio,
                    onClick = { onNavigate(Screen.Inicio) }
                )
                BottomNavItem(
                    icon = Icons.Default.RestaurantMenu,
                    isSelected = currentScreen is Screen.Menu,
                    onClick = { onNavigate(Screen.Menu) }
                )

                BottomNavItem(
                    icon = Icons.Default.Person,
                    isSelected = false,
                    onClick = { }
                )
                BottomNavItem(
                    icon = Icons.Default.Settings,
                    isSelected = false,
                    onClick = { }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(26.dp)
        )
    }
}
