package com.edu.ucne.parrilladalos2carnales.presentacion.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Screen : NavKey {

    @Serializable
    data object Login : Screen()

    @Serializable
    data object Register : Screen()

    @Serializable
    data object Menu : Screen()

    @Serializable
    data object Inicio : Screen()

    @Serializable
    data object Carrito : Screen()

    @Serializable
    data object Perfil : Screen()

    @Serializable
    data object Historial : Screen()

    @Serializable
    data class PlatoDetail(val idPlato: Int) : Screen()

    @Serializable
    data object AdminPlatoList : Screen()

    @Serializable
    data class AdminPlatoEntry(val idPlato: Int = 0) : Screen()

    @Serializable
    data object AdminGuarnicionList : Screen()

    @Serializable
    data class AdminGuarnicionEntry(val idGuarnicion: Int = 0) : Screen()


    @Serializable
    data object AdminComponenteList : Screen()

    @Serializable
    data object AdminDashboard : Screen()


    @Serializable
    data class AdminComponenteEntry(val idComponente: Int = 0) : Screen()

    @Serializable
    data object AdminPedidos : Screen()

    @Serializable
    data object Pago : Screen()

    @Serializable
    data class ConfirmacionPedido(val idPedido: Int) : Screen()

    @Serializable
    data class Seguimiento(val idPedido: Int) : Screen()
}
