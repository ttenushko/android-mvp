package com.ttenushko.androidmvp.demo.di.module

import com.ttenushko.androidmvp.demo.di.annotation.ApplicationScope
import com.ttenushko.androidmvp.demo.presentation.screens.home.HomeRouter
import com.ttenushko.androidmvp.demo.presentation.screens.home.HomeRouterProxy
import dagger.Module
import dagger.Provides

@Module
class RouterModule {

    @Provides
    @ApplicationScope
    fun provideHomeRouterProxy(): HomeRouterProxy =
        HomeRouterProxy()

    @Provides
    @ApplicationScope
    fun provideHomeRouter(homeRouterProxy: HomeRouterProxy): HomeRouter =
        homeRouterProxy
}