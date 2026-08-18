package com.edu.ucne.parrilladalos2carnales.presentacion.menu.detalle

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

data class PlatoDetalleUiState(
    val isLoading: Boolean = false,

    val plato: Plato? = null,
    val ofertaActiva: Oferta? = null,

    val guarnicionesDisponibles: List<Guarnicion> = emptyList(),
    val salsasDisponibles: List<Componente> = emptyList(),
    val terminosCoccionDisponibles: List<Componente> = emptyList(),

    // Incluidos con el plato
    val guarnicionSeleccionada: Guarnicion? = null,
    val salsaSeleccionada: Componente? = null,
    val terminoSeleccionado: Componente? = null,

    // Se cobran
    val guarnicionesExtraSeleccionadas: List<Guarnicion> = emptyList(),
    val salsasExtraSeleccionadas: List<Componente> = emptyList(),

    val cantidad: Int = 1,

    val precioExtrasUnitario: Double = 0.0,
    val precioTotal: Double = 0.0,

    val error: String? = null,
    val agregadoExitosamente: Boolean = false
)
