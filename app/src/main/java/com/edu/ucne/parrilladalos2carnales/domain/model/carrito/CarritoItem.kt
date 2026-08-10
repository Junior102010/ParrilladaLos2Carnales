package com.edu.ucne.parrilladalos2carnales.domain.model.carrito


import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

data class CarritoItem(
    val idCarritoItem: Long,
    val plato: Plato,
    val termino: Componente?,
    val guarnicion: Guarnicion?,
    val salsa: Componente?,
    val cantidad: Int,
    val precioUnitario: Double
) {
    val subtotal: Double
        get() = precioUnitario * cantidad
}
