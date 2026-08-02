package com.edu.ucne.parrilladalos2carnales.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.edu.ucne.parrilladalos2carnales.data.plato.local.PlatoDao
import com.edu.ucne.parrilladalos2carnales.data.plato.local.PlatoEntity

@Database(
    entities = [PlatoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ParrilladaDb : RoomDatabase() {
    abstract fun platoDao(): PlatoDao
}