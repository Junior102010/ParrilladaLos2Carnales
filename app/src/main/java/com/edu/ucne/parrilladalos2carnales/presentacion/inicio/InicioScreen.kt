package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria
import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.ParrilladaBottomBar
import com.edu.ucne.parrilladalos2carnales.presentacion.navigation.Screen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    viewModel: InicioViewModel = hiltViewModel(),
    onNavigate: (Screen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val ofertasList = remember(uiState.ofertas) {
        if (uiState.ofertas.isNotEmpty()) {
            uiState.ofertas
        } else {
            listOf(
                Oferta(idOferta = 1, titulo = "Parrillada Especial 1", descuentoPorcentaje = 20.0),
                Oferta(idOferta = 2, titulo = "Combo Carnal Angus", descuentoPorcentaje = 15.0),
                Oferta(idOferta = 3, titulo = "Especial de Costillas", descuentoPorcentaje = 25.0)
            )
        }
    }

    val pagerState = rememberPagerState(pageCount = { ofertasList.size })

    // Transición automática cada 3 segundos
    LaunchedEffect(ofertasList) {
        if (ofertasList.isNotEmpty()) {
            while (true) {
                delay(3000L)
                val nextPage = (pagerState.currentPage + 1) % ofertasList.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Inicio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                )
            }
        },
        bottomBar = {
            ParrilladaBottomBar(
                currentScreen = Screen.Inicio,
                rolUsuario = Rol.CLIENTE,
                onNavigate = onNavigate
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text(
                    text = "Ofertas Especiales",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // Carrusel Pager de Ofertas
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    pageSpacing = 12.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) { page ->
                    OfertaCardItem(oferta = ofertasList[page])
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Indicadores de Puntos (Dots)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(ofertasList.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(if (isSelected) 10.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                )
                        )
                    }
                }

                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.categorias) { categoria ->
                        CategoriaCardItem(
                            categoria = categoria,
                            onClick = { onNavigate(Screen.Menu) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OfertaCardItem(oferta: Oferta) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = oferta.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${oferta.descuentoPorcentaje}% OFF",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CategoriaCardItem(categoria: Categoria, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔥", style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = categoria.nombreCategoria,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}