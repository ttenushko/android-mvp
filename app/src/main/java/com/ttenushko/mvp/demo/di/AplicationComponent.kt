package com.ttenushko.mvp.demo.di

import android.content.Context
import com.squareup.picasso.Picasso
import com.ttenushko.mvp.demo.App
import com.ttenushko.mvp.demo.di.annotation.ApplicationScope
import com.ttenushko.mvp.demo.di.dependency.ComponentDependencies
import com.ttenushko.mvp.demo.di.dependency.ComponentDependenciesKey
import com.ttenushko.mvp.demo.di.module.ApplicationModule
import com.ttenushko.mvp.demo.di.module.DataModule
import com.ttenushko.mvp.demo.di.module.RouterModule
import com.ttenushko.mvp.demo.domain.application.repository.ApplicationSettings
import com.ttenushko.mvp.demo.domain.weather.repository.WeatherForecastRepository
import com.ttenushko.mvp.demo.presentation.di.annotation.ApplicationContext
import com.ttenushko.mvp.demo.presentation.screens.home.HomeRouter
import com.ttenushko.mvp.demo.presentation.screens.home.HomeRouterProxy
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.multibindings.IntoMap

interface ApplicationDependencies :
    ComponentDependencies {
    fun picasso(): Picasso
    fun applicationSettings(): ApplicationSettings
    fun weatherForecastRepository(): WeatherForecastRepository
    fun homeRouter(): HomeRouter
    fun homeRouterProxy(): HomeRouterProxy
}

@Component(
    modules = [
        ApplicationModule::class,
        DataModule::class,
        RouterModule::class,
        ComponentDependenciesModule::class
    ]
)
@ApplicationScope
interface ApplicationComponent : ApplicationDependencies {

    @Component.Builder
    interface Builder {

        @BindsInstance
        @ApplicationContext
        fun applicationContext(applicationContext: Context): Builder

        fun applicationModule(applicationModule: ApplicationModule): Builder
        fun dataModule(dataModule: DataModule): Builder
        fun build(): ApplicationComponent
    }

    fun inject(app: App)
}

@dagger.Module
abstract class ComponentDependenciesModule {
    @Binds
    @IntoMap
    @ComponentDependenciesKey(ApplicationDependencies::class)
    abstract fun applicationDependencies(component: ApplicationComponent): ComponentDependencies
}