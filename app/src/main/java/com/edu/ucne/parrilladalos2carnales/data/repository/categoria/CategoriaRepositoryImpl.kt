package com.edu.ucne.parrilladalos2carnales.data.repository.categoria

import com.edu.ucne.parrilladalos2carnales.data.categoria.local.CategoriaDao
import com.edu.ucne.parrilladalos2carnales.data.categoria.mapper.toDomain
import com.edu.ucne.parrilladalos2carnales.data.categoria.mapper.toEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria
import com.edu.ucne.parrilladalos2carnales.domain.repository.categoria.CategoriaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoriaRepositoryImpl @Inject constructor(
    private val categoriaDao: CategoriaDao
) : CategoriaRepository {
    override suspend fun upsertCategoria(categoria: Categoria) {
        categoriaDao.save(categoria.toEntity())
    }

    override suspend fun deleteCategoria(categoria: Categoria) {
        categoriaDao.delete(categoria.toEntity())
    }

    override fun getCategoria(idCategoria: Int): Flow<Categoria?> {
        return categoriaDao.getCategoria(idCategoria).map { it?.toDomain() }
    }

    override fun getCategorias(): Flow<List<Categoria>> {
        return categoriaDao.getCategorias().map { entities -> entities.map { it.toDomain() } }
    }
}