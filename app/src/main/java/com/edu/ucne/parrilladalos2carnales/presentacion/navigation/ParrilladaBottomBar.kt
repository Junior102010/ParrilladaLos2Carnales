package com.edu.ucne.parrilladalos2carnales.presentacion.navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import kotlin.math.abs

private const val BOTTOM_NAV_ANIMATION_DURATION = 280
private const val BOTTOM_NAV_ITEM_COUNT = 4

private val bottomNavIndicatorSize = 50.dp
private val bottomNavIndicatorIndex = Animatable(0f)
private val smoothBottomNavEasing =
    CubicBezierEasing(0.2f, 0f, 0f, 1f)

private fun Screen.bottomNavIndex(rolUsuario: Rol): Int =
    if (rolUsuario == Rol.ADMINISTRADOR) {
        when (this) {
            is Screen.AdminDashboard -> 0
            is Screen.AdminPedidos -> 1

            is Screen.AdminPlatoList,
            is Screen.AdminPlatoEntry,
            is Screen.AdminGuarnicionList,
            is Screen.AdminGuarnicionEntry,
            is Screen.AdminComponenteList,
            is Screen.AdminComponenteEntry -> 2

            else -> 3
        }
    } else {
        when (this) {
            is Screen.Inicio -> 0
            is Screen.Menu -> 1
            is Screen.Carrito -> 2
            else -> 3
        }
    }

@Composable
fun ParrilladaBottomBar(
    currentScreen: Screen,
    rolUsuario: Rol,
    onNavigate: (Screen) -> Unit
) {
    val selectedIndex = currentScreen.bottomNavIndex(rolUsuario)

    LaunchedEffect(selectedIndex) {
        bottomNavIndicatorIndex.animateTo(
            targetValue = selectedIndex.toFloat(),
            animationSpec = tween(
                durationMillis = BOTTOM_NAV_ANIMATION_DURATION,
                easing = smoothBottomNavEasing
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(30.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            shadowElevation = 8.dp,
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline.copy(
                    alpha = 0.75f
                )
            )
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                val itemWidthPx =
                    constraints.maxWidth.toFloat() /
                        BOTTOM_NAV_ITEM_COUNT

                val indicatorSizePx =
                    with(LocalDensity.current) {
                        bottomNavIndicatorSize.toPx()
                    }

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(bottomNavIndicatorSize)
                        .graphicsLayer {
                            translationX =
                                itemWidthPx *
                                    bottomNavIndicatorIndex.value +
                                    (
                                        itemWidthPx -
                                            indicatorSizePx
                                        ) * 0.5f
                        }
                        .background(
                            color = MaterialTheme
                                .colorScheme
                                .primary
                                .copy(alpha = 0.75f),
                            shape = CircleShape
                        )
                )

                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (rolUsuario == Rol.ADMINISTRADOR) {
                        FloatingBottomNavItem(
                            modifier = Modifier.weight(1f),
                            itemIndex = 0,
                            icon = Icons.Default.GridView,
                            contentDescription = "Dashboard",
                            onClick = {
                                onNavigate(Screen.AdminDashboard)
                            }
                        )

                        FloatingBottomNavItem(
                            modifier = Modifier.weight(1f),
                            itemIndex = 1,
                            icon = Icons.Outlined.Restaurant,
                            contentDescription = "Pedidos",
                            onClick = {
                                onNavigate(Screen.AdminPedidos)
                            }
                        )

                        FloatingBottomNavItem(
                            modifier = Modifier.weight(1f),
                            itemIndex = 2,
                            icon = Icons.Outlined.MenuBook,
                            contentDescription = "Menú",
                            onClick = {
                                onNavigate(Screen.AdminPlatoList)
                            }
                        )

                        FloatingBottomNavItem(
                            modifier = Modifier.weight(1f),
                            itemIndex = 3,
                            icon = Icons.Outlined.Person,
                            contentDescription = "Perfil",
                            onClick = { }
                        )
                    } else {
                        FloatingBottomNavItem(
                            modifier = Modifier.weight(1f),
                            itemIndex = 0,
                            icon = Icons.Default.Home,
                            contentDescription = "Inicio",
                            onClick = {
                                onNavigate(Screen.Inicio)
                            }
                        )

                        FloatingBottomNavItem(
                            modifier = Modifier.weight(1f),
                            itemIndex = 1,
                            icon = Icons.Default.RestaurantMenu,
                            contentDescription = "Menú",
                            onClick = {
                                onNavigate(Screen.Menu)
                            }
                        )

                        FloatingBottomNavItem(
                            modifier = Modifier.weight(1f),
                            itemIndex = 2,
                            icon = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito",
                            onClick = {
                                onNavigate(Screen.Carrito)
                            }
                        )

                        FloatingBottomNavItem(
                            modifier = Modifier.weight(1f),
                            itemIndex = 3,
                            icon = Icons.Default.Person,
                            contentDescription = "Perfil",
                            onClick = {
                                onNavigate(Screen.Perfil)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingBottomNavItem(
    modifier: Modifier = Modifier,
    itemIndex: Int,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    val distanceFromIndicator = abs(
        bottomNavIndicatorIndex.value -
            itemIndex.toFloat()
    )

    val selectionFraction =
        (1f - distanceFromIndicator)
            .coerceIn(0f, 1f)

    val iconColor = lerp(
        start = MaterialTheme.colorScheme.onSurface,
        stop = MaterialTheme.colorScheme.onPrimary,
        fraction = selectionFraction
    )

    val iconScale =
        1f + (0.06f * selectionFraction)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier
                .size(24.dp)
                .scale(iconScale)
        )
    }
}
