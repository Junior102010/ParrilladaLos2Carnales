package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.ComponenteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetComponenteUseCase @Inject constructor(
    private val componenteRepository: ComponenteRepository
) {
    operator fun invoke(): Flow<List<Componente>> {
        return componenteRepository.getComponentes()
    }
}