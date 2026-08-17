package com.edu.ucne.parrilladalos2carnales.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val DarkColorScheme = darkColorScheme(
    background = FondoPrincipalOscuro,
    onBackground = TextoUsuarioOscuro,


    surface = ContenedorFormularioOscuro,
    onSurface = TextoUsuarioOscuro,


    surfaceVariant = FondoCamposTextoOscuro,
    onSurfaceVariant = TextoAyudaOscuro,


    outline = TrazoContenedorOscuro,
    outlineVariant = TrazoContenedorOscuro,


    primary = BotonPrincipalOscuro,
    onPrimary = TextoBotonBlanco,


    primaryContainer = PasoActivoOscuro,
    onPrimaryContainer = TextoUsuarioOscuro,


    secondary = BotonSecundarioOscuro,
    onSecondary = TextoUsuarioOscuro,


    secondaryContainer = BotonSecundarioOscuro,
    onSecondaryContainer = TextoUsuarioOscuro,


    tertiary = TextoEnlaceOscuro
)


private val LightColorScheme = lightColorScheme(
    background = FondoPrincipalClaro,
    onBackground = TextoUsuarioClaro,


    surface = ContenedorFormularioClaro,
    onSurface = TextoUsuarioClaro,


    surfaceVariant = FondoCamposTextoClaro,
    onSurfaceVariant = TextoAyudaClaro,


    outline = TrazoContenedorClaro,
    outlineVariant = TrazoCamposTextoClaro,


    primary = BotonPrincipalClaro,
    onPrimary = TextoBotonBlanco,


    primaryContainer = PasoActivoClaro,
    onPrimaryContainer = TextoUsuarioClaro,


    secondary = BotonSecundarioClaro,
    onSecondary = TextoUsuarioClaro,


    secondaryContainer = BotonSecundarioClaro,
    onSecondaryContainer = TextoUsuarioClaro,


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
