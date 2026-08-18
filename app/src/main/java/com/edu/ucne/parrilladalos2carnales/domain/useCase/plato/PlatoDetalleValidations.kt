package com.edu.ucne.parrilladalos2carnales.domain.useCase.plato


data class PlatoDetalleValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)


fun validateSeleccionDetalle(
    tieneGuarniciones: Boolean,
    guarnicionSeleccionada: Boolean,


    tieneSalsas: Boolean,
    salsaSeleccionada: Boolean,


    tieneTerminos: Boolean,
    terminoSeleccionado: Boolean
): PlatoDetalleValidations {


    return when {


        tieneGuarniciones &&
        !guarnicionSeleccionada ->


            PlatoDetalleValidations(
                false,
                "Selecciona una guarnición"
            )


        tieneSalsas &&
        !salsaSeleccionada ->


            PlatoDetalleValidations(
                false,
                "Selecciona una salsa"
            )


        tieneTerminos &&
        !terminoSeleccionado ->


            PlatoDetalleValidations(
                false,
                "Selecciona el término de la carne"
            )


        else ->
            PlatoDetalleValidations(
                true
            )
    }
}
