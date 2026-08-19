package com.edu.ucne.parrilladalos2carnales

import app.cash.turbine.test
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.ComponenteRepository
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente.UpsertComponenteUseCase
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente.AdminComponenteUiEvent
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente.AdminComponenteViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminComponenteViewModelTest {

    private lateinit var viewModel: AdminComponenteViewModel
    private val upsertComponenteUseCase: UpsertComponenteUseCase = mockk()
    private val componenteRepository: ComponenteRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = AdminComponenteViewModel(upsertComponenteUseCase, componenteRepository)
    }

    @Test
    fun `onEvent OnNombreChange actualiza el estado`() = runTest {
        val nuevoNombre = "Sal de Mar"
        
        viewModel.onEvent(AdminComponenteUiEvent.OnNombreChange(nuevoNombre))
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(nuevoNombre, state.nombreComponente)
        }
    }

    @Test
    fun `onEvent OnCategoriaChange a Coccion pone precio a 0`() = runTest {
        viewModel.onEvent(AdminComponenteUiEvent.OnPrecioChange("10.0"))
        viewModel.onEvent(AdminComponenteUiEvent.OnCategoriaChange("Coccion"))
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("0.0", state.precioComponente)
            assertEquals("Coccion", state.categoriaComponente)
        }
    }
}
