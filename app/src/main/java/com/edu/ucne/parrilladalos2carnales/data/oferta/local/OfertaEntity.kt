package com.edu.ucne.parrilladalos2carnales.data.oferta.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ofertas")
data class OfertaEntity(
    @PrimaryKey(autoGenerate = true)
    val idOferta: Int = 0,
    val tituloOferta: String = "",
    val descripcionOferta: String = "",
    val descuento: Double = 0.0,
    val imagenUrl: String = "",
    val idPlato: Int? = null,
    val activa: Boolean = true
)
