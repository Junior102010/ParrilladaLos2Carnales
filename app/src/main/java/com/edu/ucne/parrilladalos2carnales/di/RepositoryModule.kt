package com.edu.ucne.parrilladalos2carnales.di

import com.edu.ucne.parrilladalos2carnales.data.repository.AuthRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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
}