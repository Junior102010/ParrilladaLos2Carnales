package com.edu.ucne.parrilladalos2carnales.data.plato.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Platos")
data class PlatoEntity(
    @PrimaryKey(autoGenerate = true)
    val idPlato: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val imagenUrl: String = "",
    val idCategoria: Int = 0,
    val disponible: Boolean = true
)
