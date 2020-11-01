package com.ttenushko.mvp.demo.presentation.screens.home

class HomeRouterProxy : HomeRouter {

    private var router: HomeRouter? = null
    private val pendingDestinations = mutableListOf<HomeRouter.Destination>()

    override fun navigateTo(destination: HomeRouter.Destination) {
        router?.navigateTo(destination) ?: pendingDestinations.add(destination)
    }

    fun attach(router: HomeRouter) {
        require(null == this.router) { "Router already attached" }
        this.router = router
        pendingDestinations.removeAll { destination ->
            router.navigateTo(destination)
            true
        }
    }

    fun detach(router: HomeRouter) {
        require(router === this.router) { "Trying to detach incorrect router" }
        this.router = null
    }
}