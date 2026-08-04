package com.edu.ucne.parrilladalos2carnales.presentacion.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.edit.AdminPlatoEntryScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.edit.AdminPlatoEntryViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.list.AdminPlatoListScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.list.AdminPlatoListViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.inicio.InicioScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.inicio.InicioViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.login.LoginScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.login.LoginViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.menu.MenuScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.plato.list.PlatoListViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.register.RegisterScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.register.RegisterViewModel
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol

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
                            backStack.add(Screen.AdminPlatoList)
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
                            backStack.add(Screen.AdminPlatoList)
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

            entry<Screen.Menu> {
                val platoListViewModel: PlatoListViewModel = hiltViewModel()
                MenuScreen(
                    viewModel = platoListViewModel,
                    onNavigate = handleNavigation,
                    onPlatoClick = { }
                )
            }

            entry<Screen.AdminPlatoList> {
                val adminListViewModel: AdminPlatoListViewModel = hiltViewModel()
                AdminPlatoListScreen(
                    viewModel = adminListViewModel,
                    onNavigateToCreate = { backStack.add(Screen.AdminPlatoEntry(idPlato = 0)) },
                    onNavigateToEdit = { id -> backStack.add(Screen.AdminPlatoEntry(idPlato = id)) }
                )
            }

            entry<Screen.AdminPlatoEntry> {
                val adminEntryViewModel: AdminPlatoEntryViewModel = hiltViewModel()
                AdminPlatoEntryScreen(
                    viewModel = adminEntryViewModel,
                    onBack = { if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1) }
                )
            }
        }
    )
}