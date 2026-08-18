package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatosUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.oferta.GetOfertasUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.categoria.GetCategoriasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InicioViewModel @Inject constructor(
    private val getPlatosUseCase: GetPlatosUseCase,
    private val getOfertasUseCase: GetOfertasUseCase,
    private val getCategoriasUseCase: GetCategoriasUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        refrescarUsuario()
        loadCatalogoInicio()
        loadOfertas()
    }

    fun onEvent(event: InicioUiEvent) {
        when (event) {
            InicioUiEvent.OnRefresh -> {
                loadCatalogoInicio()
                loadOfertas()
            }
        }
    }

    private fun loadCatalogoInicio() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            combine(
                getPlatosUseCase(),
                getCategoriasUseCase()
            ) { platos, categorias ->
                val platosDisponibles = platos.filter { it.disponible }
                Pair(platosDisponibles, categorias)
            }.catch { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "No se pudo cargar el menú"
                    )
                }
            }.collect { (platos, categorias) ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        platos = platos,
                        categorias = categorias,
                        errorMessage = null
                    )
                }
            }
        }
    }

    private fun loadOfertas() {
        viewModelScope.launch {
            getOfertasUseCase()
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            errorMessage = error.localizedMessage
                        )
                    }
                }
                .collect { lista ->
                    _uiState.update {
                        it.copy(
                            ofertas = lista
                                .filter { oferta -> oferta.activa }
                                .sortedByDescending { oferta -> oferta.idOferta }
                        )
                    }
                }
        }
    }

    fun refrescarUsuario() {
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
