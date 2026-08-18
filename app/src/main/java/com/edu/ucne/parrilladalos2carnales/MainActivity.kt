package com.edu.ucne.parrilladalos2carnales


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
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

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            // No necesitamos hacer nada aquí.
        }


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)


        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {


            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

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
