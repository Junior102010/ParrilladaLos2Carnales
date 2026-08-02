package com.edu.ucne.parrilladalos2carnales.data.plato.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PlatoDao {
    @Upsert
    suspend fun save(plato: PlatoEntity)

    @Delete
    suspend fun delete(plato: PlatoEntity)

    @Query("SELECT * FROM Platos WHERE idPlato = :idPlato LIMIT 1")
    fun getPlato(idPlato: Int): Flow<PlatoEntity?>

    @Query("SELECT * FROM Platos")
    fun getAll(): Flow<List<PlatoEntity>>
}