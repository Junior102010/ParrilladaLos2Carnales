package com.edu.ucne.parrilladalos2carnales.domain.useCase.plato

import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.repository.plato.PlatoRepository
import javax.inject.Inject

class DeletePlatoUseCase @Inject constructor(
    private val platoRepository: PlatoRepository
) {
    suspend operator fun invoke(plato: Plato) {
        platoRepository.deletePlato(plato)
    }
}