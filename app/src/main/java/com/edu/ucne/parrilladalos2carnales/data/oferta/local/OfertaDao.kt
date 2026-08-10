package com.edu.ucne.parrilladalos2carnales.data.oferta.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface OfertaDao {
    @Upsert
    suspend fun save(oferta: OfertaEntity)

    @Delete
    suspend fun delete(oferta: OfertaEntity)

    @Query("SELECT * FROM Ofertas WHERE idOferta = :idOferta")
    fun getOferta(idOferta: Int): Flow<OfertaEntity?>

    @Query("SELECT * FROM Ofertas")
    fun getOfertas(): Flow<List<OfertaEntity>>
}
