package com.edu.ucne.parrilladalos2carnales.presentacion.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol

@Composable
fun ParrilladaBottomBar(
    currentScreen: Screen,
    rolUsuario: Rol,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            NavItem(
                icon = Icons.Default.Home,
                isSelected = currentScreen is Screen.Inicio,
                onClick = { onNavigate(Screen.Inicio) }
            )


            if (rolUsuario == Rol.CLIENTE) {
                NavItem(
                    icon = Icons.Default.RestaurantMenu,
                    isSelected = currentScreen is Screen.Menu,
                    onClick = { onNavigate(Screen.Menu) }
                )
                NavItem(
                    icon = Icons.Default.ShoppingCart,
                    isSelected = currentScreen is Screen.Carrito,
                    onClick = { onNavigate(Screen.Carrito) }
                )
            } else {

                NavItem(
                    icon = Icons.Default.Settings,
                    isSelected = currentScreen is Screen.AdminPlatoList,
                    onClick = { onNavigate(Screen.AdminPlatoList) }
                )

            }


            NavItem(
                icon = Icons.Default.Person,
                isSelected = currentScreen is Screen.Perfil,
                onClick = { onNavigate(Screen.Perfil) }
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else androidx.compose.ui.graphics.Color.Transparent
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}