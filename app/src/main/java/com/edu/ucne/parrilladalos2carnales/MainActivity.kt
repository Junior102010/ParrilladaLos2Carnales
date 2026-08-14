package com.edu.ucne.parrilladalos2carnales


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaNavDisplay
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import com.edu.ucne.parrilladalos2carnales.ui.theme.ParrilladaLos2CarnalesTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var authRepository: AuthRepository


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()


        setContent {
            ParrilladaLos2CarnalesTheme {
                val initialScreen: NavKey =
                    when {
                        !authRepository
                            .isUsuarioLogueado() -> {
                            Screen.Login
                        }


                        authRepository
                            .esAdministrador() -> {
                            Screen.AdminDashboard
                        }


                        else -> {
                            Screen.Inicio
                        }
                    }


                val backStack =
                    rememberNavBackStack(
                        initialScreen
                    )


                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ParrilladaNavDisplay(
                        backStack = backStack,
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}
