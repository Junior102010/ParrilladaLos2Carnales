package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

sealed interface InicioUiEvent {
    data object OnRefresh : InicioUiEvent
}