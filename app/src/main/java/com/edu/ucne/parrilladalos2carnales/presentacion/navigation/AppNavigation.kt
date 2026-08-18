package com.edu.ucne.parrilladalos2carnales.presentacion.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import com.edu.ucne.parrilladalos2carnales.presentacion.historial.HistorialScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.historial.HistorialViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.inicio.InicioScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.inicio.InicioViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.login.LoginScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.login.LoginViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.menu.list.MenuScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.menu.detalle.PlatoDetalleScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.menu.detalle.PlatoDetalleViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.notificacion.NotificacionViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.notificacion.NotificacionesScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.PagoScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.pago.PagoViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.perfil.PerfilScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.perfil.PerfilViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.perfil.editar.EditarPerfilScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.perfil.editar.EditarPerfilViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.plato.list.PlatoListViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.registro.RegisterScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.registro.RegisterViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.seguimiento.SeguimientoScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.seguimiento.SeguimientoViewModel

private const val NAVIGATION_ANIMATION_DURATION = 280
private const val NAVIGATION_FADE_DURATION = 200
private val SmoothNavigationEasing = CubicBezierEasing(0.3f, 0f, 0.1f, 1f)


private fun forwardNavigationTransition(): ContentTransform =
    (
        slideInHorizontally(
            initialOffsetX = { fullWidth ->
                fullWidth / 10
            },
            animationSpec = tween(
                durationMillis = NAVIGATION_ANIMATION_DURATION,
                easing = SmoothNavigationEasing
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = NAVIGATION_FADE_DURATION,
                easing = SmoothNavigationEasing
            )
        )
    ) togetherWith (
        slideOutHorizontally(
            targetOffsetX = { fullWidth ->
                -fullWidth / 20
            },
            animationSpec = tween(
                durationMillis = NAVIGATION_ANIMATION_DURATION,
                easing = SmoothNavigationEasing
            )
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = NAVIGATION_FADE_DURATION,
                easing = SmoothNavigationEasing
            )
        )
    )


private fun backwardNavigationTransition(): ContentTransform =
    (
        slideInHorizontally(
            initialOffsetX = { fullWidth ->
                -fullWidth / 10
            },
            animationSpec = tween(
                durationMillis = NAVIGATION_ANIMATION_DURATION,
                easing = SmoothNavigationEasing
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = NAVIGATION_FADE_DURATION,
                easing = SmoothNavigationEasing
            )
        )
    ) togetherWith (
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth / 10 },
            animationSpec = tween(
                durationMillis = NAVIGATION_ANIMATION_DURATION,
                easing = SmoothNavigationEasing
            )
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = NAVIGATION_FADE_DURATION,
                easing = SmoothNavigationEasing
            )
        )
    )


@Composable
fun ParrilladaNavDisplay(
    backStack: NavBackStack<NavKey>,
    innerPadding: PaddingValues = PaddingValues()
) {
    val handleNavigation: (Screen) -> Unit = { screen ->
        backStack.add(screen)
    }

    val openAuthenticatedStart: (Rol) -> Unit = { rol ->
        while (backStack.isNotEmpty()) {
            backStack.removeAt(
                backStack.lastIndex
            )
        }


        backStack.add(
            if (rol == Rol.ADMINISTRADOR) {
                Screen.AdminDashboard
            } else {
                Screen.Inicio
            }
        )
    }

    NavDisplay(
        backStack = backStack,
        transitionSpec = { forwardNavigationTransition() },
        popTransitionSpec = { backwardNavigationTransition() },
        predictivePopTransitionSpec = { backwardNavigationTransition() },
        entryProvider = entryProvider {

            entry<Screen.Login> {
                val loginViewModel: LoginViewModel =
                    hiltViewModel()


                LoginScreen(
                    viewModel = loginViewModel,
                    onNavigateToRegister = {
                        backStack.add(
                            Screen.Register
                        )
                    },
                    onLoginSuccess =
                        openAuthenticatedStart
                )
            }

            entry<Screen.Register> {
                val registerViewModel: RegisterViewModel =
                    hiltViewModel()


                RegisterScreen(
                    viewModel = registerViewModel,
                    onBack = {
                        if (backStack.isNotEmpty()) {
                            backStack.removeAt(
                                backStack.lastIndex
                            )
                        }
                    },
                    onRegisterSuccess =
                        openAuthenticatedStart
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
                    rolUsuario = Rol.CLIENTE,
                    onLogout = {
                        while (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                        backStack.add(Screen.Login)
                    }
                )
            }

            entry<Screen.AdminPerfil> {
                val perfilViewModel: PerfilViewModel = hiltViewModel()
                PerfilScreen(
                    viewModel = perfilViewModel,
                    onNavigate = handleNavigation,
                    rolUsuario = Rol.ADMINISTRADOR,
                    onLogout = {
                        while (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                        backStack.add(Screen.Login)
                    }
                )
            }

            entry<Screen.EditarPerfil> { editarPerfil ->


                val viewModel:
                    EditarPerfilViewModel =
                    hiltViewModel()


                LaunchedEffect(
                    editarPerfil.esAdministrador
                ) {


                    viewModel
                        .refrescarUsuario()
                }


                EditarPerfilScreen(
                    viewModel =
                        viewModel,


                    onBack = {


                        if (
                            backStack.isNotEmpty()
                        ) {


                            backStack.removeAt(
                                backStack.lastIndex
                            )
                        }
                    },


                    onGuardado = {


                        if (
                            backStack.isNotEmpty()
                        ) {


                            backStack.removeAt(
                                backStack.lastIndex
                            )
                        }
                    }
                )
            }

            entry<Screen.Historial> {
                val viewModel: HistorialViewModel = hiltViewModel()
                HistorialScreen(
                    viewModel = viewModel,
                    onBack = {
                        if (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    },
                    onSeguimiento = { idPedido ->
                        backStack.add(Screen.Seguimiento(idPedido = idPedido))
                    },
                    onNavigate = handleNavigation
                )
            }

            entry<Screen.Menu> {
                val platoListViewModel: PlatoListViewModel = hiltViewModel()
                MenuScreen(
                    viewModel = platoListViewModel,
                    titulo = "Menú",
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
                        backStack.add(Screen.Seguimiento(idPedido = idPedido))
                    }
                )
            }

            entry<Screen.Seguimiento> { seguimiento ->
                val viewModel: SeguimientoViewModel = hiltViewModel()
                LaunchedEffect(seguimiento.idPedido) {
                    viewModel.setPedidoId(seguimiento.idPedido)
                }
                SeguimientoScreen(
                    viewModel = viewModel,
                    onNavigate = handleNavigation
                )
            }

            entry<Screen.MenuCategoria> { menuCategoria ->
                val viewModel: PlatoListViewModel = hiltViewModel()
                LaunchedEffect(menuCategoria.idCategoria) {
                    viewModel.setCategoria(menuCategoria.idCategoria)
                }
                MenuScreen(
                    viewModel = viewModel,
                    titulo = menuCategoria.nombreCategoria,
                    onNavigate = handleNavigation,
                    onPlatoClick = { idPlato ->
                        backStack.add(Screen.PlatoDetail(idPlato = idPlato))
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

            entry<Screen.AdminPlatoEntry> { entry ->
                val adminEntryViewModel: AdminPlatoEntryViewModel = hiltViewModel()

                LaunchedEffect(entry.idPlato) {
                    adminEntryViewModel.prepararEntrada(entry.idPlato)
                }

                AdminPlatoEntryScreen(
                    viewModel = adminEntryViewModel,
                    onBack = {
                        if (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    }
                )
            }

            entry<Screen.AdminGuarnicionList> {
                val viewModel: AdminGuarnicionListViewModel = hiltViewModel()
                AdminGuarnicionListScreen(
                    viewModel = viewModel,
                    onNavigateToAdd = { backStack.add(Screen.AdminGuarnicionEntry(idGuarnicion = 0)) },
                    onNavigateToEdit = { id -> backStack.add(Screen.AdminGuarnicionEntry(idGuarnicion = id)) },
                    onNavigate = handleNavigation
                )
            }

            entry<Screen.AdminGuarnicionEntry> { entry ->
                val viewModel: AdminGuarnicionViewModel = hiltViewModel()

                LaunchedEffect(entry.idGuarnicion) {
                    viewModel.prepararEntrada(entry.idGuarnicion)
                }

                AdminGuarnicionScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        if (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    }
                )
            }

            entry<Screen.AdminComponenteList> {
                val viewModel: AdminComponenteListViewModel = hiltViewModel()
                AdminComponenteListScreen(
                    viewModel = viewModel,
                    onNavigateToAdd = { backStack.add(Screen.AdminComponenteEntry(idComponente = 0)) },
                    onNavigateToEdit = { id -> backStack.add(Screen.AdminComponenteEntry(idComponente = id)) },
                    onNavigate = handleNavigation
                )
            }

            entry<Screen.AdminComponenteEntry> { entry ->
                val viewModel: AdminComponenteViewModel = hiltViewModel()

                LaunchedEffect(entry.idComponente) {
                    viewModel.prepararEntrada(entry.idComponente)
                }

                AdminComponenteScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        if (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    }
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

            entry<Screen.Notificaciones> {
                val viewModel: NotificacionViewModel = hiltViewModel()

                NotificacionesScreen(
                    viewModel = viewModel,
                    onBack = {
                        if (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    },
                    onNotificacionClick = {
                        // después aquí abrimos
                        // pedido/oferta correspondiente
                    }
                )
            }
        }
    )
}
