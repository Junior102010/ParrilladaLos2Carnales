package com.edu.ucne.parrilladalos2carnales.domain.repository.categoria

import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria
import kotlinx.coroutines.flow.Flow

interface CategoriaRepository {
    suspend fun upsertCategoria(categoria: Categoria)
    suspend fun deleteCategoria(categoria: Categoria)
    fun getCategoria(idCategoria: Int): Flow<Categoria?>
    fun getCategorias(): Flow<List<Categoria>>
}
