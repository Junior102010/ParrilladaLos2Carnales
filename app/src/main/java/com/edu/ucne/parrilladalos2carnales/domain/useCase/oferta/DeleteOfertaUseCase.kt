package com.edu.ucne.parrilladalos2carnales.domain.useCase.oferta

import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import com.edu.ucne.parrilladalos2carnales.domain.repository.oferta.OfertaRepository
import javax.inject.Inject

class DeleteOfertaUseCase @Inject constructor(
    private val ofertaRepository: OfertaRepository
) {
    suspend operator fun invoke(oferta: Oferta) {
        ofertaRepository.deleteOferta(oferta)
    }
}
