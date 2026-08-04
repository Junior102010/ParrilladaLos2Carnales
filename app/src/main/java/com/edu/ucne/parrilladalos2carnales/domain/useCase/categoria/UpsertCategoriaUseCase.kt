package com.edu.ucne.parrilladalos2carnales.domain.useCase.categoria

import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria
import com.edu.ucne.parrilladalos2carnales.domain.repository.categoria.CategoriaRepository
import javax.inject.Inject

class UpsertCategoriaUseCase @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) {
    suspend operator fun invoke(categoria: Categoria): Result<Unit> {
        val nombreResult = validateNombreCategoria(categoria.nombreCategoria)

        if (!nombreResult.isValid) {
            return Result.failure(IllegalArgumentException(nombreResult.errorMessage))
        }

        return runCatching { categoriaRepository.upsertCategoria(categoria) }
    }
}