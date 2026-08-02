package com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Guarniciones")
data class GuarnicionEntity(
    @PrimaryKey(autoGenerate = true)
    val idGuarnicion: Int = 0,
    val nombreGuarnicion: String = "",
    val descripcionGuarnicion: String = "",
    val precioGuarnicion: Double = 0.0,
    val categoria: String = "",
    val cantidad: Double = 0.0,
    val disponible: Boolean = true
)