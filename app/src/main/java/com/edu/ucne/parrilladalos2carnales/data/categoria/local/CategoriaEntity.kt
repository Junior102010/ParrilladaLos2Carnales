package com.edu.ucne.parrilladalos2carnales.data.categoria.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categorias")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val idCategoria: Int = 0,
    val nombreCategoria: String = "",
    val descripcionCategoria: String = "",
    val imagenUrl: String = ""
)