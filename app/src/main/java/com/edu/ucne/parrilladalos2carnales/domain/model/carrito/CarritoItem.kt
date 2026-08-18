package com.edu.ucne.parrilladalos2carnales.domain.model.carrito

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

data class CarritoItem(
    val idCarritoItem: Long,
    val plato: Plato,

    // Selecciones incluidas
    val termino: Componente?,
    val guarnicion: Guarnicion?,
    val salsa: Componente?,

    // Extras que sí se cobran
    val guarnicionesExtra: List<Guarnicion> = emptyList(),
    val salsasExtra: List<Componente> = emptyList(),

    val cantidad: Int,
    val precioUnitario: Double
) {
    val subtotal: Double
        get() = precioUnitario * cantidad

    val precioExtrasUnitario: Double
        get() =
            guarnicionesExtra.sumOf { it.precioGuarnicion } +
            salsasExtra.sumOf { it.precioComponente }
}
