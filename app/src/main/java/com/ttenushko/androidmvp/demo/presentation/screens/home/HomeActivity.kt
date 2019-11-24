package com.ttenushko.androidmvp.demo.presentation.screens.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ttenushko.androidmvp.demo.R
import com.ttenushko.androidmvp.demo.di.dependency.ComponentDependenciesProvider
import com.ttenushko.androidmvp.demo.di.dependency.HasComponentDependencies
import com.ttenushko.androidmvp.demo.di.module.UseCaseModule
import com.ttenushko.androidmvp.demo.presentation.base.activity.BaseActivity
import com.ttenushko.androidmvp.demo.presentation.di.utils.findComponentDependencies
import com.ttenushko.androidmvp.demo.presentation.screens.home.di.DaggerHomeActivityComponent

import javax.inject.Inject

class HomeActivity : BaseActivity(), HasComponentDependencies {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, HomeActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                )
            }
            context.startActivity(intent)
        }
    }

    @Suppress("ProtectedInFinal")
    @Inject
    override lateinit var dependencies: ComponentDependenciesProvider
        protected set
    @Inject
    protected lateinit var homeRouterProxy: HomeRouterProxy
    private lateinit var homeRouter: HomeRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerHomeActivityComponent.builder()
            .applicationDependencies(findComponentDependencies())
            .useCaseModule(UseCaseModule())
            .build()
            .inject(this)

        setContentView(R.layout.activity_home)
        homeRouter = HomeRouterImpl(this.findNavController(R.id.nav_host_fragment))
    }

    override fun onStart() {
        super.onStart()
        homeRouterProxy.attach(homeRouter)
    }

    override fun onStop() {
        super.onStop()
        homeRouterProxy.detach(homeRouter)
    }

    override fun onNavigationBackStackChanged() {
        super.onNavigationBackStackChanged()
        navigationTopmostFragment?.view?.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            val navController = findNavController(R.id.nav_host_fragment)
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            setSupportActionBar(toolbar)
            toolbar.setupWithNavController(navController, appBarConfiguration)
            setupActionBarWithNavController(navController)
        }
    }
}