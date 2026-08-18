package com.edu.ucne.parrilladalos2carnales.presentacion.notificacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.data.repository.notificacion.NotificacionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotificacionViewModel @Inject constructor(

    private val repository:
        NotificacionRepository

) : ViewModel() {

    val uiState =
        repository
            .notificaciones
            .map {

                NotificacionUiState(
                    notificaciones = it
                )
            }
            .stateIn(
                scope = viewModelScope,

                started =
                    SharingStarted.WhileSubscribed(
                        5_000
                    ),

                initialValue =
                    NotificacionUiState()
            )

    fun marcarLeida(
        id: Long
    ) {

        repository
            .marcarComoLeida(
                id
            )
    }

    fun marcarTodasLeidas() {

        repository
            .marcarTodasComoLeidas()
    }

    fun eliminar(
        id: Long
    ) {

        repository
            .eliminar(
                id
            )
    }
}
