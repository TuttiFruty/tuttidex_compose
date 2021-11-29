package fr.tuttifruty.pokeapp.common.di

import org.koin.dsl.module

val commonModule = module {
}

val appModule =
    listOf(viewModelModule, useCaseModule, repositoryModule, serviceModule, commonModule, roomDataSourceModule)
val mockedAppModule =
    listOf(viewModelModule, useCaseModule, repositoryModule, mockedServiceModule, commonModule, roomDataSourceModule)


