package com.jisu98.order.di

import com.jisu98.order.data.repository.CartRepositoryImpl
import com.jisu98.order.data.repository.MenuRepositoryImpl
import com.jisu98.order.domain.repository.CartRepository
import com.jisu98.order.domain.repository.MenuRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindMenuRepository(impl: MenuRepositoryImpl): MenuRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
}
