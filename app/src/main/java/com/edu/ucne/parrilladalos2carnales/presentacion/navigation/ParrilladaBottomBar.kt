package com.edu.ucne.parrilladalos2carnales.presentacion.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol

@Composable
fun ParrilladaBottomBar(
    currentScreen: Screen,
    rolUsuario: Rol,
    onNavigate: (Screen) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),

        contentAlignment =
            Alignment.Center
    ) {

        Surface(
            shape =
                RoundedCornerShape(30.dp),



            color =
                MaterialTheme.colorScheme.surface,

            tonalElevation = 0.dp,

            shadowElevation = 8.dp,

            border =
                BorderStroke(
                    width = 2.dp,
                    color =
                        MaterialTheme
                            .colorScheme
                            .outline
                            .copy(
                                alpha = 0.75f
                            )
                ),

            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxSize(),

                horizontalArrangement =
                    Arrangement.SpaceEvenly,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                if (
                    rolUsuario ==
                    Rol.ADMINISTRADOR
                ) {

                    FloatingBottomNavItem(
                        icon =
                            Icons.Default.GridView,

                        contentDescription =
                            "Dashboard",

                        isSelected =
                            currentScreen
                                    is Screen.AdminDashboard,

                        onClick = {
                            onNavigate(
                                Screen.AdminDashboard
                            )
                        }
                    )

                    FloatingBottomNavItem(
                        icon =
                            Icons.Outlined.Restaurant,

                        contentDescription =
                            "Pedidos",

                        isSelected =
                            currentScreen
                                    is Screen.AdminPedidos,

                        onClick = {
                            onNavigate(
                                Screen.AdminPedidos
                            )
                        }
                    )

                    FloatingBottomNavItem(
                        icon =
                            Icons.Outlined.MenuBook,

                        contentDescription =
                            "Menú",

                        isSelected =
                            currentScreen
                                    is Screen.AdminPlatoList ||
                                    currentScreen
                                            is Screen.AdminPlatoEntry,

                        onClick = {
                            onNavigate(
                                Screen.AdminPlatoList
                            )
                        }
                    )

                    FloatingBottomNavItem(
                        icon =
                            Icons.Outlined.Person,

                        contentDescription =
                            "Perfil",

                        isSelected = false,

                        onClick = { }
                    )

                } else {

                    FloatingBottomNavItem(
                        icon =
                            Icons.Default.Home,

                        contentDescription =
                            "Inicio",

                        isSelected =
                            currentScreen
                                    is Screen.Inicio,

                        onClick = {
                            onNavigate(
                                Screen.Inicio
                            )
                        }
                    )

                    FloatingBottomNavItem(
                        icon =
                            Icons.Default
                                .RestaurantMenu,

                        contentDescription =
                            "Menú",

                        isSelected =
                            currentScreen
                                    is Screen.Menu,

                        onClick = {
                            onNavigate(
                                Screen.Menu
                            )
                        }
                    )

                    FloatingBottomNavItem(
                        icon =
                            Icons.Default
                                .ShoppingCart,

                        contentDescription =
                            "Carrito",

                        isSelected =
                            currentScreen
                                    is Screen.Carrito,

                        onClick = {
                            onNavigate(
                                Screen.Carrito
                            )
                        }
                    )

                    FloatingBottomNavItem(
                        icon =
                            Icons.Default.Person,

                        contentDescription =
                            "Perfil",

                        isSelected =
                            currentScreen
                                    is Screen.Perfil,

                        onClick = {
                            onNavigate(
                                Screen.Perfil
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingBottomNavItem(
    icon: ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val backgroundColor by
    animateColorAsState(

        targetValue =
            if (isSelected) {

                MaterialTheme
                    .colorScheme
                    .primary
                    .copy(
                        alpha = 0.75f
                    )

            } else {

                Color.Transparent
            },

        animationSpec =
            tween(250),

        label =
            "BottomNavBackground"
    )

    val iconColor by
    animateColorAsState(

        targetValue =
            if (isSelected) {

                MaterialTheme
                    .colorScheme
                    .onPrimary

            } else {

                MaterialTheme
                    .colorScheme
                    .onSurface
            },

        animationSpec =
            tween(250),

        label =
            "BottomNavIcon"
    )

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(
                backgroundColor
            )
            .clickable(
                interactionSource =
                    remember {
                        MutableInteractionSource()
                    },

                indication = null,

                onClick = onClick
            ),

        contentAlignment =
            Alignment.Center
    ) {

        Icon(
            imageVector = icon,

            contentDescription =
                contentDescription,

            tint = iconColor,

            modifier =
                Modifier.size(24.dp)
        )
    }
}
