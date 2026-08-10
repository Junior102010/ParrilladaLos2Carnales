package com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.mapper

import com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.local.ComponenteEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente

fun ComponenteEntity.toDomain(): Componente {
    return Componente(
        idComponente = this.idComponente,
        nombreComponente = this.nombreComponente,
        descripcionComponente = this.descripcionComponente,
        precioComponente = this.precioComponente,
        categoriaComponente = this.categoriaComponente,
        cantidadComponente = this.cantidadComponente,
        coccion = this.coccion,
        disponible = this.disponible

    )
}

fun Componente.toEntity(): ComponenteEntity {
    return ComponenteEntity(
        idComponente = this.idComponente,
        nombreComponente = this.nombreComponente,
        descripcionComponente = this.descripcionComponente,
        precioComponente = this.precioComponente,
        categoriaComponente = this.categoriaComponente,
        disponible = this.disponible
    )
}
