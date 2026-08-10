package com.edu.ucne.parrilladalos2carnales.data.categoria.mapper

import com.edu.ucne.parrilladalos2carnales.data.categoria.local.CategoriaEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria

fun CategoriaEntity.toDomain(): Categoria {
    return Categoria(
        idCategoria = this.idCategoria,
        nombreCategoria = this.nombreCategoria,
        descripcionCategoria = this.descripcionCategoria,
        imagenUrl = this.imagenUrl
    )
}

fun Categoria.toEntity(): CategoriaEntity {
    return CategoriaEntity(
        idCategoria = this.idCategoria,
        nombreCategoria = this.nombreCategoria,
        descripcionCategoria = this.descripcionCategoria,
        imagenUrl = this.imagenUrl
    )
}
