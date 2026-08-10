package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun InicioScreen(
    viewModel: InicioViewModel = hiltViewModel(),
    onNavigate: (Screen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val ofertas = remember(uiState.ofertas) {
        if (uiState.ofertas.isNotEmpty()) {
            uiState.ofertas
        } else {
            listOf(
                Oferta(
                    idOferta = 1,
                    titulo = "Parrillada Especial",
                    descuentoPorcentaje = 20.0
                ),
                Oferta(
                    idOferta = 2,
                    titulo = "Combo Carnales",
                    descuentoPorcentaje = 15.0
                ),
                Oferta(
                    idOferta = 3,
                    titulo = "Especial de Cortes",
                    descuentoPorcentaje = 25.0
                ),
                Oferta(
                    idOferta = 4,
                    titulo = "Especial de la Casa",
                    descuentoPorcentaje = 10.0
                )
            )
        }
    }

    val pagerState = rememberPagerState(
        pageCount = { ofertas.size }
    )

    LaunchedEffect(ofertas.size) {
        if (ofertas.size > 1) {
            while (true) {
                delay(3500)

                val siguientePagina =
                    (pagerState.currentPage + 1) % ofertas.size

                pagerState.animateScrollToPage(siguientePagina)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            InicioTopBar(
                fotoUsuario = uiState.fotoUsuario,
                onPerfilClick = {
                    onNavigate(Screen.Perfil)
                }
            )
        },

        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = Screen.Inicio,
                rolUsuario = Rol.CLIENTE,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Text(
                text = "Hola, ${uiState.nombreUsuario}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(155.dp)
            ) { pagina ->

                OfertaInicioCard(
                    oferta = ofertas[pagina]
                )
            }

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                repeat(ofertas.size) { index ->

                    val seleccionado =
                        pagerState.currentPage == index

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .size(
                                if (seleccionado) {
                                    10.dp
                                } else {
                                    9.dp
                                }
                            )
                            .background(
                                color = if (seleccionado) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outlineVariant
                                },
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            CategoriasInicio(
                onCategoriaClick = {
                    onNavigate(Screen.Menu)
                }
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            if (uiState.platos.isNotEmpty()) {

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(
                        start = 4.dp,
                        end = 4.dp
                    )
                ) {

                    items(
                        items = uiState.platos,
                        key = { plato -> plato.idPlato }
                    ) { plato ->

                        ProductoInicioCard(
                            plato = plato,
                            onClick = {
                                onNavigate(
                                    Screen.PlatoDetail(
                                        idPlato = plato.idPlato
                                    )
                                )
                            },
                            onAddClick = {
                                // Después conectamos esto al carrito.
                            }
                        )
                    }
                }

            } else {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "No hay platos disponibles",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }


        }
    }
}