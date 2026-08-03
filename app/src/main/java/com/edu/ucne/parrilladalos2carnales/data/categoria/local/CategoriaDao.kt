package com.edu.ucne.parrilladalos2carnales.data.categoria.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.edu.ucne.parrilladalos2carnales.data.categoria.local.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Upsert
    suspend fun save(categoria: CategoriaEntity)

    @Delete
    suspend fun delete(categoria: CategoriaEntity)

    @Query("SELECT * FROM Categorias WHERE idCategoria = :idCategoria")
    fun getCategoria(idCategoria: Int): Flow<CategoriaEntity?>

    @Query("SELECT * FROM Categorias")
    fun getCategorias(): Flow<List<CategoriaEntity>>
}