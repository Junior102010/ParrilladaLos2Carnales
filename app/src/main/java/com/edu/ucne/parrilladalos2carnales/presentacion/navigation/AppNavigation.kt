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
import com.edu.ucne.parrilladalos2carnales.presentacion.login.LoginScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.login.LoginViewModel
import com.edu.ucne.parrilladalos2carnales.presentacion.register.RegisterScreen
import com.edu.ucne.parrilladalos2carnales.presentacion.register.RegisterViewModel


@Composable
fun ParrilladaNavDisplay(
    backStack: NavBackStack<NavKey>,
    innerPadding: PaddingValues = PaddingValues()
) {
    NavDisplay(
        backStack = backStack,
        modifier = Modifier.padding(innerPadding),
        entryProvider = entryProvider {

            entry<Screen.Login> {
                val loginViewModel: LoginViewModel = hiltViewModel()
                LoginScreen(
                    viewModel = loginViewModel,
                    onNavigateToRegister = {
                        backStack.add(Screen.Register)
                    },
                    onLoginSuccess = {
                        backStack.add(Screen.Menu)
                    }
                )
            }

            entry<Screen.Register> {
                val registerViewModel: RegisterViewModel = hiltViewModel()
                RegisterScreen(
                    viewModel = registerViewModel,
                    onBack = {
                        if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1)
                    },
                    onRegisterSuccess = {
                        backStack.add(Screen.Menu)
                    }
                )
            }

            entry<Screen.Menu> {

            }
        }
    )
}