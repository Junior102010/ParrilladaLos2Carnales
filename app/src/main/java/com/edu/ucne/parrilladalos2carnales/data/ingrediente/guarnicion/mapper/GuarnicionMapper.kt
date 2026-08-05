package com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.mapper

import com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.local.GuarnicionEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion

fun GuarnicionEntity.toDomain(): Guarnicion {
    return Guarnicion(
        idGuarnicion = this.idGuarnicion,
        nombreGuarnicion = this.nombreGuarnicion,
        descripcionGuarnicion = this.descripcionGuarnicion,
        precioGuarnicion = this.precioGuarnicion,
        categoria = this.categoria,
        cantidadGuarnicion = this.cantidad,
        disponible = this.disponible
    )
}

fun Guarnicion.toEntity(): GuarnicionEntity {
    return GuarnicionEntity(
        idGuarnicion = this.idGuarnicion,
        nombreGuarnicion = this.nombreGuarnicion,
        descripcionGuarnicion = this.descripcionGuarnicion,
        precioGuarnicion = this.precioGuarnicion,
        categoria = this.categoria,
        cantidad = this.cantidadGuarnicion,
        disponible = this.disponible
    )
}