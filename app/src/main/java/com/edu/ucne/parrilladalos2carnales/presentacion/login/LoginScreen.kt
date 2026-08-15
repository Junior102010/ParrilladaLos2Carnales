package com.edu.ucne.parrilladalos2carnales.presentacion.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.ucne.parrilladalos2carnales.R
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import kotlin.math.sin

private const val FULL_ROTATION_RADIANS = 6.2831855f


private data class LoginFlameSpec(
    val xFraction: Float,
    val heightFraction: Float,
    val delay: Float,
    val swayDirection: Float
)


private val loginFlameSpecs = listOf(
    LoginFlameSpec(0.22f, 0.16f, 0.05f, -1f),
    LoginFlameSpec(0.35f, 0.21f, 0.42f, 1f),
    LoginFlameSpec(0.50f, 0.25f, 0.18f, -1f),
    LoginFlameSpec(0.65f, 0.21f, 0.68f, 1f),
    LoginFlameSpec(0.78f, 0.16f, 0.84f, -1f)
)

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (Rol) -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var isContentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isContentVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isContentVisible,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 420)
            ) + slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight / 10 },
                animationSpec = tween(
                    durationMillis = 420,
                    easing = FastOutSlowInEasing
                )
            ) + scaleIn(
                initialScale = 0.97f,
                animationSpec = tween(
                    durationMillis = 420,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .widthIn(max = 420.dp),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedFireLogo(
                        modifier = Modifier.size(172.dp),
                        logoSize = 148.dp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    LoginTextField(
                        value = viewModel.username,
                        onValueChange = { viewModel.username = it },
                        placeholder = "Usuario",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LoginTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        placeholder = "Contraseña",
                        visualTransformation = if (viewModel.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.onLoginClick(onLoginSuccess) }),
                        trailingIcon = {
                            val icon = if (viewModel.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { viewModel.onTogglePasswordVisibility() }) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = "Toggle Password Visibility",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    )

                    viewModel.errorMessage?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.onLoginClick(onLoginSuccess) },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        enabled = !viewModel.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(text = "Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    OutlinedButton(
                        onClick = {
                            focusManager.clearFocus()


                            viewModel.loginConGoogle(
                                context = context,
                                onSuccess = onLoginSuccess
                            )
                        },
                        modifier = Modifier
                            .widthIn(min = 200.dp)
                            .height(48.dp),
                        enabled = !viewModel.isLoading,
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Image(
                            painter = painterResource(
                                id = R.drawable.logo_de_google
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )


                        Spacer(modifier = Modifier.width(10.dp))


                        Text(
                            text = "Continuar con Google",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = onNavigateToRegister,
                        enabled = !viewModel.isLoading
                    ) {
                        Text(
                            text = "¿No tienes una cuenta? ¡Regístrate!",
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedFireLogo(
    modifier: Modifier = Modifier,
    logoSize: Dp = 148.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "FireAnimation")

    val swayProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = FULL_ROTATION_RADIANS,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Sway"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val path = Path()
            loginFlameSpecs.forEach { spec ->
                val sway = sin(swayProgress + (spec.delay * FULL_ROTATION_RADIANS)) * 12f * spec.swayDirection
                path.setFlameShape(
                    baseX = size.width * spec.xFraction,
                    baseY = size.height * 0.72f,
                    flameWidth = size.width * 0.16f,
                    flameHeight = size.height * spec.heightFraction,
                    sway = sway
                )
                drawPath(
                    path = path,
                    color = androidx.compose.ui.graphics.Color(0xFFFF5722).copy(alpha = 0.6f)
                )
            }

            for (i in 0 until 12) {
                val progress = ((swayProgress / FULL_ROTATION_RADIANS) + (i / 12f)) % 1f
                val emberX = size.width * 0.5f + sin(progress * FULL_ROTATION_RADIANS + i) * (size.width * 0.35f)
                val emberY = size.height * 0.72f - progress * (size.height * 0.6f)

                drawCircle(
                    color = androidx.compose.ui.graphics.Color(0xFFFFB74D).copy(alpha = 1f - progress),
                    radius = 3f,
                    center = androidx.compose.ui.geometry.Offset(
                        x = emberX,
                        y = emberY
                    )
                )
            }
        }


        Image(
            painter = painterResource(
                id = R.drawable.parrillada_sin_fondo
            ),
            contentDescription = "Logo Los Dos Carnales",
            modifier = Modifier.size(logoSize),
            contentScale = ContentScale.Fit
        )
    }
}


private fun Path.setFlameShape(
    baseX: Float,
    baseY: Float,
    flameWidth: Float,
    flameHeight: Float,
    sway: Float
) {
    reset()


    val halfWidth = flameWidth / 2f
    val tipX = baseX + sway
    val tipY = baseY - flameHeight


    moveTo(
        x = baseX - halfWidth,
        y = baseY
    )


    cubicTo(
        x1 = baseX - halfWidth * 0.85f,
        y1 = baseY - flameHeight * 0.34f,
        x2 = tipX - halfWidth * 0.18f,
        y2 = baseY - flameHeight * 0.78f,
        x3 = tipX,
        y3 = tipY
    )


    cubicTo(
        x1 = tipX + halfWidth * 0.22f,
        y1 = baseY - flameHeight * 0.72f,
        x2 = baseX + halfWidth * 0.88f,
        y2 = baseY - flameHeight * 0.34f,
        x3 = baseX + halfWidth,
        y3 = baseY
    )


    cubicTo(
        x1 = baseX + halfWidth * 0.30f,
        y1 = baseY - flameHeight * 0.10f,
        x2 = baseX - halfWidth * 0.30f,
        y2 = baseY - flameHeight * 0.10f,
        x3 = baseX - halfWidth,
        y3 = baseY
    )


    close()
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true
    )
}
