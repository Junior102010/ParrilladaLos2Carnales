package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.oferta

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import com.edu.ucne.parrilladalos2carnales.domain.useCase.oferta.GetOfertasUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.oferta.UpsertOfertaUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.oferta.DeleteOfertaUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminOfertaViewModel @Inject constructor(
    private val getOfertasUseCase: GetOfertasUseCase,
    private val getPlatosUseCase: GetPlatosUseCase,
    private val upsertOfertaUseCase: UpsertOfertaUseCase,
    private val deleteOfertaUseCase: DeleteOfertaUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminOfertaUiState())
    val uiState: StateFlow<AdminOfertaUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            combine(
                getOfertasUseCase(),
                getPlatosUseCase()
            ) { ofertas, platos ->
                _uiState.update { state ->
                    filtrar(
                        state.copy(
                            ofertas = ofertas,
                            platos = platos.sortedBy { it.nombre.lowercase() },
                            isLoading = false
                        )
                    )
                }
            }.catch { error ->
                _uiState.update { it.copy(errorMessage = error.localizedMessage, isLoading = false) }
            }.collect()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { filtrar(it.copy(searchQuery = query)) }
    }

    fun onEditorVisibleChanged(visible: Boolean) {
        _uiState.update { it.copy(editorVisible = visible) }
    }

    fun iniciarNuevaOferta() {
        _uiState.update {
            it.copy(
                editorVisible = true,
                idOfertaEditando = 0,
                tituloOferta = "",
                descripcionOferta = "",
                descuento = "",
                imagenUrl = "",
                idPlatoSeleccionado = null,
                activa = true,
                errorMessage = null
            )
        }
    }

    fun iniciarEdicionOferta(oferta: Oferta) {
        _uiState.update {
            it.copy(
                editorVisible = true,
                idOfertaEditando = oferta.idOferta,
                tituloOferta = oferta.tituloOferta,
                descripcionOferta = oferta.descripcionOferta,
                descuento = oferta.descuento.toString(),
                imagenUrl = oferta.imagenUrl,
                idPlatoSeleccionado = oferta.idPlato,
                activa = oferta.activa,
                errorMessage = null
            )
        }
    }

    fun onTituloChanged(titulo: String) {
        _uiState.update { it.copy(tituloOferta = titulo) }
    }

    fun onDescripcionChanged(descripcion: String) {
        _uiState.update { it.copy(descripcionOferta = descripcion) }
    }

    fun onDescuentoChanged(descuento: String) {
        _uiState.update { it.copy(descuento = descuento) }
    }

    fun onImagenSelected(uriString: String) {
        try {
            val uri = Uri.parse(uriString)
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            _uiState.update {
                it.copy(
                    imagenUrl = uriString,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    errorMessage = "No se pudo conservar el acceso a la imagen"
                )
            }
        }
    }

    fun onPlatoSelected(idPlato: Int?) {
        _uiState.update { it.copy(idPlatoSeleccionado = idPlato) }
    }

    fun onActivaChanged(activa: Boolean) {
        _uiState.update { it.copy(activa = activa) }
    }

    fun guardarOferta() {
        val state = _uiState.value
        val descuento = state.descuento.toDoubleOrNull()
        val plato = state.platos.find { it.idPlato == state.idPlatoSeleccionado }

        if (state.tituloOferta.trim().length < 3) {
            _uiState.update { it.copy(errorMessage = "El título debe tener al menos 3 caracteres") }
            return
        }

        if (state.idPlatoSeleccionado == null) {
            _uiState.update { it.copy(errorMessage = "Selecciona el plato al que se aplicará la oferta") }
            return
        }

        if (descuento == null) {
            _uiState.update { it.copy(errorMessage = "Introduce un descuento válido") }
            return
        }

        if (descuento <= 0.0 || descuento > 100.0) {
            _uiState.update { it.copy(errorMessage = "El descuento debe estar entre 1% y 100%") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val oferta = Oferta(
                idOferta = state.idOfertaEditando,
                tituloOferta = state.tituloOferta.trim(),
                descripcionOferta = state.descripcionOferta.trim(),
                descuento = descuento,
                imagenUrl = state.imagenUrl.trim().ifBlank { plato?.imagenUrl ?: "" },
                idPlato = state.idPlatoSeleccionado,
                activa = state.activa
            )
            val result = upsertOfertaUseCase(oferta)
            result.onSuccess {
                _uiState.update { it.copy(isSaving = false, editorVisible = false) }
            }.onFailure { error ->
                _uiState.update { it.copy(isSaving = false, errorMessage = error.localizedMessage) }
            }
        }
    }

    fun eliminarOferta(oferta: Oferta) {
        viewModelScope.launch {
            deleteOfertaUseCase(oferta)
        }
    }

    private fun filtrar(state: AdminOfertaUiState): AdminOfertaUiState {
        val query = state.searchQuery.trim()
        if (query.isBlank()) {
            return state.copy(ofertasFiltradas = state.ofertas)
        }

        val platosPorId = state.platos.associate { it.idPlato to it.nombre }

        val filtradas = state.ofertas.filter { oferta ->
            val nombrePlato = oferta.idPlato?.let { platosPorId[it] }.orEmpty()
            oferta.tituloOferta.contains(query, ignoreCase = true) ||
                    oferta.descripcionOferta.contains(query, ignoreCase = true) ||
                    nombrePlato.contains(query, ignoreCase = true)
        }

        return state.copy(ofertasFiltradas = filtradas)
    }
}
