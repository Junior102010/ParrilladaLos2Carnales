package com.edu.ucne.parrilladalos2carnales.presentacion.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminDashboard.AdminDashboardScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminDashboard.AdminDashboardViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminPedido.AdminPedidosScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminPedido.AdminPedidosViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente.AdminComponenteScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente.AdminComponenteViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente.list.AdminComponenteListScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente.list.AdminComponenteListViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion.AdminGuarnicionScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion.AdminGuarnicionViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion.list.AdminGuarnicionListScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion.list.AdminGuarnicionListViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.edit.AdminPlatoEntryScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.edit.AdminPlatoEntryViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.list.AdminPlatoListScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.list.AdminPlatoListViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.carrito.CarritoScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.carrito.CarritoViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.confirmacion.ConfirmacionPedidoScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.confirmacion.ConfirmacionPedidoViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.inicio.InicioScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.inicio.InicioViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.login.LoginScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.login.LoginViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.menu.MenuScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.menu.detalle.PlatoDetalleScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.menu.detalle.PlatoDetalleViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.PagoScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.PagoViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.perfil.PerfilScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.perfil.PerfilViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.plato.list.PlatoListViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.registro.RegisterScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.registro.RegisterViewModel

@Composable
fun ParrilladaNavDisplay(
    backStack: NavBackStack<NavKey>,
    innerPadding: PaddingValues = PaddingValues()
) {
    val handleNavigation: (Screen) -> Unit = { screen ->
        backStack.add(screen)
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {

            entry<Screen.Login> {
                val loginViewModel: LoginViewModel = hiltViewModel()
                LoginScreen(
                    viewModel = loginViewModel,
                    onNavigateToRegister = { backStack.add(Screen.Register) },
                    onLoginSuccess = { rol: Rol ->
                        if (rol == Rol.ADMINISTRADOR) {
                            backStack.add(Screen.AdminDashboard)
                        } else {
                            backStack.add(Screen.Inicio)
                        }
                    }
                )
            }

            entry<Screen.Register> {
                val registerViewModel: RegisterViewModel = hiltViewModel()
                RegisterScreen(
                    viewModel = registerViewModel,
                    onBack = { if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1) },
                    onRegisterSuccess = { rol: Rol ->
                        if (rol == Rol.ADMINISTRADOR) {
                            backStack.add(Screen.AdminDashboard)
                        } else {
                            backStack.add(Screen.Inicio)
                        }
                    }
                )
            }

            entry<Screen.Inicio> {
                val inicioViewModel: InicioViewModel = hiltViewModel()
                InicioScreen(
                    viewModel = inicioViewModel,
                    onNavigate = handleNavigation
                )
            }

            entry<Screen.Carrito> {
                val carritoViewModel: CarritoViewModel = hiltViewModel()
                CarritoScreen(
                    viewModel = carritoViewModel,
                    onNavigate = handleNavigation
                )
            }

            entry<Screen.Perfil> {
                val perfilViewModel: PerfilViewModel = hiltViewModel()
                PerfilScreen(
                    viewModel = perfilViewModel,
                    onNavigate = handleNavigation,
                    onLogout = {
                        while (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                        backStack.add(Screen.Login)
                    }
                )
            }

            entry<Screen.Menu> {
                val platoListViewModel: PlatoListViewModel = hiltViewModel()
                MenuScreen(
                    viewModel = platoListViewModel,
                    onNavigate = handleNavigation,
                    onPlatoClick = { idPlato ->
                        backStack.add(Screen.PlatoDetail(idPlato = idPlato))
                    }
                )
            }

            entry<Screen.PlatoDetail> { platoDetail ->
                val platoDetailViewModel: PlatoDetalleViewModel = hiltViewModel()
                

                LaunchedEffect(platoDetail.idPlato) {
                    platoDetailViewModel.setId(platoDetail.idPlato)
                }

                PlatoDetalleScreen(
                    viewModel = platoDetailViewModel,

                    onBack = {
                        if (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    },
                    onAgregadoAlCarrito = {
                        backStack.add(Screen.Carrito)
                    }
                )
            }
            entry<Screen.Pago> {
                val pagoViewModel: PagoViewModel = hiltViewModel()
                PagoScreen(
                    viewModel = pagoViewModel,
                    onBack = { if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1) },
                    onPedidoCreado = { idPedido ->
                        if (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                        backStack.add(Screen.ConfirmacionPedido(idPedido = idPedido))
                    }
                )
            }

            entry<Screen.ConfirmacionPedido> { confirmacion ->
                val viewModel: ConfirmacionPedidoViewModel = hiltViewModel()
                LaunchedEffect(confirmacion.idPedido) {
                    viewModel.setId(confirmacion.idPedido)
                }
                ConfirmacionPedidoScreen(
                    viewModel = viewModel,
                    onVolverInicio = {
                        while (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                        backStack.add(Screen.Inicio)
                    },
                    onVerEstado = { idPedido ->
                        // Pendiente: SeguimientoPedido
                    }
                )
            }

            entry<Screen.AdminPlatoList> {
                val adminListViewModel: AdminPlatoListViewModel = hiltViewModel()
                AdminPlatoListScreen(
                    viewModel = adminListViewModel,
                    onNavigateToCreate = { backStack.add(Screen.AdminPlatoEntry(idPlato = 0)) },
                    onNavigateToEdit = { id -> backStack.add(Screen.AdminPlatoEntry(idPlato = id)) },
                    onNavigate = handleNavigation
                )
            }

            entry<Screen.AdminPlatoEntry> {
                val adminEntryViewModel: AdminPlatoEntryViewModel = hiltViewModel()
                AdminPlatoEntryScreen(
                    viewModel = adminEntryViewModel,
                    onBack = { if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1) }
                )
            }

            entry<Screen.AdminGuarnicionList> {
                val viewModel: AdminGuarnicionListViewModel = hiltViewModel()
                AdminGuarnicionListScreen(
                    viewModel = viewModel,
                    onNavigateToAdd = { backStack.add(Screen.AdminGuarnicionEntry(idGuarnicion = 0)) },
                    onNavigateToEdit = { id -> backStack.add(Screen.AdminGuarnicionEntry(idGuarnicion = id)) },
                    onNavigateBack = { if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1) },
                    onNavigate = handleNavigation
                )
            }

            entry<Screen.AdminGuarnicionEntry> {
                val viewModel: AdminGuarnicionViewModel = hiltViewModel()
                AdminGuarnicionScreen(
                    viewModel = viewModel,
                    onNavigateBack = { if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1) }
                )
            }

            entry<Screen.AdminComponenteList> {
                val viewModel: AdminComponenteListViewModel = hiltViewModel()
                AdminComponenteListScreen(
                    viewModel = viewModel,
                    onNavigateToAdd = { backStack.add(Screen.AdminComponenteEntry(idComponente = 0)) },
                    onNavigateToEdit = { id -> backStack.add(Screen.AdminComponenteEntry(idComponente = id)) },
                    onNavigateBack = { if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1) },
                    onNavigate = handleNavigation
                )
            }

            entry<Screen.AdminComponenteEntry> {
                val viewModel: AdminComponenteViewModel = hiltViewModel()
                AdminComponenteScreen(
                    viewModel = viewModel,
                    onNavigateBack = { if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1) }
                )
            }

            entry<Screen.AdminDashboard> {
                val viewModel: AdminDashboardViewModel = hiltViewModel()
                AdminDashboardScreen(
                    viewModel = viewModel,
                    onNavigate = handleNavigation
                )
            }

            entry<Screen.AdminPedidos> {
                val viewModel: AdminPedidosViewModel = hiltViewModel()
                AdminPedidosScreen(
                    viewModel = viewModel,
                    onNavigate = handleNavigation
                )
            }
        }
    )
}
