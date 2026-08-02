package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente

import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.ComponenteRepository
import jakarta.inject.Inject

class DeleteComponenteUseCase @Inject constructor(
    private val repository: ComponenteRepository
){
    suspend operator fun invoke(id : Int) = repository.deleteComponente(id)
}