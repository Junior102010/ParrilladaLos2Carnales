package com.edu.ucne.parrilladalos2carnales

import app.cash.turbine.test
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.GuarnicionRepository
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion.UpsertGuarnicionUseCase
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion.AdminGuarnicionUiEvent
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion.AdminGuarnicionViewModel
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
class AdminGuarnicionViewModelTest {

    private lateinit var viewModel: AdminGuarnicionViewModel
    private val upsertGuarnicionUseCase: UpsertGuarnicionUseCase = mockk()
    private val guarnicionRepository: GuarnicionRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = AdminGuarnicionViewModel(upsertGuarnicionUseCase, guarnicionRepository)
    }

    @Test
    fun `onEvent OnNombreChange actualiza el estado`() = runTest {
        val nuevoNombre = "Papas Supremas"
        
        viewModel.onEvent(AdminGuarnicionUiEvent.OnNombreChange(nuevoNombre))
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(nuevoNombre, state.nombreGuarnicion)
        }
    }
}
