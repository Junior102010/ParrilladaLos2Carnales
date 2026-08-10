package com.edu.ucne.parrilladalos2carnales.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.edu.ucne.parrilladalos2carnales.data.repository.AuthRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.data.repository.carrito.CarritoRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.data.repository.categoria.CategoriaRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.data.repository.ingrediente.ComponenteRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.data.repository.ingrediente.GuarnicionRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.data.repository.oferta.OfertaRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.data.repository.pedido.PedidoRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.data.repository.plato.PlatoRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.domain.repository.carrito.CarritoRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.categoria.CategoriaRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.ComponenteRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.GuarnicionRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.oferta.OfertaRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.pedido.PedidoRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.plato.PlatoRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPlatoRepository(
        platoRepositoryImpl: PlatoRepositoryImpl
    ): PlatoRepository

    @Binds
    @Singleton
    abstract fun bindGuarnicionRepository(
        guarnicionRepositoryImpl: GuarnicionRepositoryImpl
    ): GuarnicionRepository

    @Binds
    @Singleton
    abstract fun bindComponenteRepository(
        componenteRepositoryImpl: ComponenteRepositoryImpl
    ): ComponenteRepository

    @Binds
    @Singleton
    abstract fun bindCategoriaRepository(
        categoriaRepositoryImpl: CategoriaRepositoryImpl
    ): CategoriaRepository

    @Binds
    @Singleton
    abstract fun bindOfertaRepository(
        ofertaRepositoryImpl: OfertaRepositoryImpl
    ): OfertaRepository

    @Binds
    @Singleton
    abstract fun bindPedidoRepository(
        pedidoRepositoryImpl: PedidoRepositoryImpl
    ): PedidoRepository

    @Binds
    @Singleton
    abstract fun bindCarritoRepository(
        carritoRepositoryImpl: CarritoRepositoryImpl
    ): CarritoRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        @Provides
        @Singleton
        fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
            return CredentialManager.create(context)
        }
    }
}
