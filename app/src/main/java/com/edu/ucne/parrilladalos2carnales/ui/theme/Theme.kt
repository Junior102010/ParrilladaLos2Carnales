package com.edu.ucne.parrilladalos2carnales.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = FondoPrincipalOscuro,
    surface = ContenedorFormularioOscuro,
    surfaceVariant = FondoCamposTextoOscuro,
    outline = TrazoContenedorOscuro,
    primary = BotonPrincipalOscuro,
    onPrimary = TextoBotonBlanco,
    secondary = BotonSecundarioOscuro,
    onBackground = TextoUsuarioOscuro,
    onSurface = TextoUsuarioOscuro,
    tertiary = TextoEnlaceOscuro
)

private val LightColorScheme = lightColorScheme(
    background = FondoPrincipalClaro,
    surface = ContenedorFormularioClaro,
    surfaceVariant = FondoCamposTextoClaro,
    outline = TrazoContenedorClaro,
    primary = BotonPrincipalClaro,
    onPrimary = TextoBotonBlanco,
    secondary = BotonSecundarioClaro,
    onBackground = TextoUsuarioClaro,
    onSurface = TextoUsuarioClaro,
    tertiary = TextoEnlaceClaro
)

@Composable
fun ParrilladaLos2CarnalesTheme(
    themeMode: ThemeMode = ThemeManager.themeMode,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SISTEMA -> isSystemInDarkTheme()
        ThemeMode.CLARO -> false
        ThemeMode.OSCURO -> true
    }

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}