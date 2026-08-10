package com.edu.ucne.parrilladalos2carnales.domain.useCase.plato

import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.repository.plato.PlatoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlatoUseCase @Inject constructor(
    private val platoRepository: PlatoRepository
) {
    operator fun invoke(idPlato: Int): Flow<Plato?> {
        return platoRepository.getPlato(idPlato)
    }
}

