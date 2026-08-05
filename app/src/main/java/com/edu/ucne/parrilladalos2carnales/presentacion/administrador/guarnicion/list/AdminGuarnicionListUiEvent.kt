package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion.list

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion

sealed interface AdminGuarnicionListUiEvent {
    data object OnAddGuarnicionClick : AdminGuarnicionListUiEvent
    data class OnEditGuarnicionClick(val idGuarnicion: Int) : AdminGuarnicionListUiEvent
    data class OnDeleteGuarnicionClick(val guarnicion: Guarnicion) : AdminGuarnicionListUiEvent
    data object OnBackClick : AdminGuarnicionListUiEvent
}