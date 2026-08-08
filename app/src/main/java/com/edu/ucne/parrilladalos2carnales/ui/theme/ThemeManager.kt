package com.edu.ucne.parrilladalos2carnales.ui.theme


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class ThemeMode(val label: String) {
    SISTEMA("Sistema (Auto)"),
    CLARO("Claro ☀️"),
    OSCURO("Oscuro 🌙")
}

object ThemeManager {
    var themeMode by mutableStateOf(ThemeMode.SISTEMA)
}