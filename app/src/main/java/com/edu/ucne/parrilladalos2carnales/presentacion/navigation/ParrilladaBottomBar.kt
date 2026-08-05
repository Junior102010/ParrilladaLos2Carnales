package com.edu.ucne.parrilladalos2carnales.presentacion.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol

private val WarmOrangePrimary = Color(0xFFF26522)
private val SubtextColor = Color(0xFF756F6A)

@Composable
fun ParrilladaBottomBar(
    currentScreen: Screen,
    rolUsuario: Rol,
    onNavigate: (Screen) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars) // 👈 Evita solapamiento con botones Android
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (rolUsuario == Rol.ADMINISTRADOR) {
                AdminTabItem(
                    title = "Dashboard",
                    icon = Icons.Default.GridView,
                    isSelected = currentScreen is Screen.AdminDashboard,
                    onClick = { onNavigate(Screen.AdminDashboard) }
                )
                AdminTabItem(
                    title = "Pedidos",
                    icon = Icons.Outlined.Restaurant,
                    isSelected = false,
                    onClick = { }
                )
                AdminTabItem(
                    title = "Menú",
                    icon = Icons.Outlined.MenuBook,
                    isSelected = currentScreen is Screen.AdminPlatoList || currentScreen is Screen.AdminPlatoEntry,
                    onClick = { onNavigate(Screen.AdminPlatoList) }
                )
                AdminTabItem(
                    title = "Perfil",
                    icon = Icons.Outlined.Person,
                    isSelected = false,
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
            }
        }
    }
}

@Composable
private fun AdminTabItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(if (isSelected) WarmOrangePrimary else Color.Transparent)
                .padding(horizontal = 20.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) Color.White else SubtextColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) WarmOrangePrimary else SubtextColor
        )
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