package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion.GetGuarnicionUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion.DeleteGuarnicionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminGuarnicionListViewModel @Inject constructor(
    private val getGuarnicionUseCase: GetGuarnicionUseCase,
    private val deleteGuarnicionUseCase: DeleteGuarnicionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminGuarnicionListUiState())
    val uiState: StateFlow<AdminGuarnicionListUiState> = _uiState.asStateFlow()

    init {
        cargarGuarniciones()
    }

    fun onEvent(event: AdminGuarnicionListUiEvent) {
        when (event) {
            is AdminGuarnicionListUiEvent.OnDeleteGuarnicionClick -> borrarGuarnicion(event.guarnicion)
            AdminGuarnicionListUiEvent.OnAddGuarnicionClick -> { /* Manejado por navegación */ }
            is AdminGuarnicionListUiEvent.OnEditGuarnicionClick -> { /* Manejado por navegación */ }
            AdminGuarnicionListUiEvent.OnBackClick -> { /* Manejado por navegación */ }
        }
    }

    private fun cargarGuarniciones() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getGuarnicionUseCase()
                .catch { e ->
                    _uiState.update { it.copy(error = e.localizedMessage, isLoading = false) }
                }
                .collect { lista ->
                    _uiState.update { it.copy(guarniciones = lista, isLoading = false) }
                }
        }
    }

    private fun borrarGuarnicion(guarnicion: Guarnicion) {
        viewModelScope.launch {
            deleteGuarnicionUseCase(guarnicion.idGuarnicion)
        }
    }
}