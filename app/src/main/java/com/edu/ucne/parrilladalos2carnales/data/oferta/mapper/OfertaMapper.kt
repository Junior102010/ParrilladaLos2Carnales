package com.edu.ucne.parrilladalos2carnales.data.oferta.mapper

import com.edu.ucne.parrilladalos2carnales.data.oferta.local.OfertaEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta

fun OfertaEntity.toDomain(): Oferta {
    return Oferta(
        idOferta = this.idOferta,
        tituloOferta = this.tituloOferta,
        descripcionOferta = this.descripcionOferta,
        descuento = this.descuento,
        imagenUrl = this.imagenUrl
    )
}

fun Oferta.toEntity(): OfertaEntity {
    return OfertaEntity(
        idOferta = this.idOferta,
        tituloOferta = this.tituloOferta,
        descripcionOferta = this.descripcionOferta,
        descuento = this.descuento,
        imagenUrl = this.imagenUrl
    )
}
