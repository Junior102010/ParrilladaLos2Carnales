package com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Componentes")
data class ComponenteEntity(
    @PrimaryKey(autoGenerate = true)
    val idComponente: Int = 0,
    val nombreComponente: String = "",
    val descripcionComponente: String = "",
    val precioComponente: Double = 0.0,
    val categoriaComponente: String = "",
    val cantidadComponente: Double = 0.0,
    val disponible: Boolean = true,
    val coccion: String = ""
)