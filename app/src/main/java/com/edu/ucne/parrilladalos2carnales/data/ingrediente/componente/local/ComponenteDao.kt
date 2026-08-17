package com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ComponenteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertComponente(componente: ComponenteEntity): Long

    @Query("SELECT * FROM Componentes WHERE idComponente = :id")
    suspend fun getComponente(id: Int): ComponenteEntity?

    @Query("SELECT * FROM Componentes")
    fun getComponentes(): Flow<List<ComponenteEntity>>

    @Delete
    suspend fun deleteComponente(componente: ComponenteEntity)

    @Query("DELETE FROM Componentes WHERE idComponente = :id")
    suspend fun deleteComponenteById(id: Int)
}
