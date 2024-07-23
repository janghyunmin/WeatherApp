package com.kkjang.data.module.ui

import com.kkjang.data.module.AppModule
import com.kkjang.data.module.NetModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module(includes = [NetModule::class, AppModule::class])
@InstallIn(SingletonComponent::class)
object WeatherModule {

}