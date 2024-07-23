package com.kkjang.data.module

import android.content.Context
import com.kkjang.data.module.display.DisplayManager
import com.kkjang.data.module.network.NetworkConnection
import com.kkjang.data.module.provider.ResourcesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDisplayManager() : DisplayManager = DisplayManager()

    @Singleton
    @Provides
    fun provideNetworkConnection(@ApplicationContext context: Context) : NetworkConnection = NetworkConnection(context = context)

    @Singleton
    @Provides
    fun provideResourcesProvider(@ApplicationContext context: Context) : ResourcesProvider = ResourcesProvider(context = context)

    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context) : Context {
        return context
    }
}