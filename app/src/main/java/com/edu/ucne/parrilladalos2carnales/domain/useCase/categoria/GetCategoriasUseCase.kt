package com.edu.ucne.parrilladalos2carnales.domain.useCase.categoria

import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria
import com.edu.ucne.parrilladalos2carnales.domain.repository.categoria.CategoriaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriasUseCase @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) {
    operator fun invoke(): Flow<List<Categoria>> {
        return categoriaRepository.getCategorias()
    }
}
