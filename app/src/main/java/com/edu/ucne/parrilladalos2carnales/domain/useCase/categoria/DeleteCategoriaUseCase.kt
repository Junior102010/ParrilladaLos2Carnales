package com.edu.ucne.parrilladalos2carnales.domain.useCase.categoria

import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria
import com.edu.ucne.parrilladalos2carnales.domain.repository.categoria.CategoriaRepository
import javax.inject.Inject

class DeleteCategoriaUseCase @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) {
    suspend operator fun invoke(categoria: Categoria) {
        categoriaRepository.deleteCategoria(categoria)
    }
}