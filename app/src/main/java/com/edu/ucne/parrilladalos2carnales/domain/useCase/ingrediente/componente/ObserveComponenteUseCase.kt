package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.ComponenteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveComponenteUseCase @Inject constructor(
    private val repository: ComponenteRepository
){
    suspend operator fun invoke() : Flow<List<Componente>> = repository.observeComponente()
}