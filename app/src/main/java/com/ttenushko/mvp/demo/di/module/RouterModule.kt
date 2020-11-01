package com.ttenushko.mvp.demo.di.module

import com.ttenushko.mvp.demo.di.annotation.ApplicationScope
import com.ttenushko.mvp.demo.presentation.screens.home.HomeRouter
import com.ttenushko.mvp.demo.presentation.screens.home.HomeRouterProxy
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