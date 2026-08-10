package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InicioViewModel @Inject constructor(
    private val getPlatosUseCase: GetPlatosUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        cargarUsuario()
        loadPlatos()
    }

    fun onEvent(event: InicioUiEvent) {
        when (event) {
            InicioUiEvent.OnRefresh -> loadPlatos()
        }
    }

    private fun loadPlatos() {

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getPlatosUseCase().catch { error ->
                _uiState.update { it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage
                        )
                    }
                }.collect { lista ->
                    _uiState.update { it.copy(
                            isLoading = false,
                            platos = lista.filter { plato ->
                                plato.disponible
                            }
                        )
                    }
                }
        }
    }

    private fun cargarUsuario() {
        val nombreCompleto = authRepository.getNombreUsuario().orEmpty().trim()
        val primerNombre = nombreCompleto.substringBefore(" ").ifBlank { "Cliente" }
        val foto = authRepository.getFotoUsuario()

        _uiState.update {
            it.copy(
                nombreUsuario = primerNombre,
                fotoUsuario = foto
            )
        }
    }
}
