package com.ttenushko.androidmvp.demo.presentation.screens.home

import androidx.navigation.NavController
import com.ttenushko.androidmvp.demo.R
import com.ttenushko.androidmvp.demo.presentation.screens.home.addplace.AddPlaceFragment
import com.ttenushko.androidmvp.demo.presentation.screens.home.placedetails.PlaceDetailsFragment
import com.ttenushko.androidmvp.demo.presentation.utils.getActionIdByDestinationId

class HomeRouterImpl(private val navController: NavController) : HomeRouter {

    override fun navigateTo(destination: HomeRouter.Destination) {
        val currentDestination =
            navController.currentDestination?.id?.let { navController.graph.findNode(it) }
        when (destination) {
            is HomeRouter.Destination.GoBack -> {
                navController.popBackStack()
            }
            is HomeRouter.Destination.AddPlace -> {
                val destinationId = R.id.addPlaceFragment
                val actionId =
                    currentDestination?.getActionIdByDestinationId(destinationId) ?: destinationId
                navController.navigate(
                    actionId,
                    AddPlaceFragment.args(destination.search)
                )
            }
            is HomeRouter.Destination.PlaceDetails -> {
                val destinationId = R.id.placeDetailsFragment
                val actionId =
                    currentDestination?.getActionIdByDestinationId(destinationId) ?: destinationId
                navController.navigate(
                    actionId,
                    PlaceDetailsFragment.args(destination.placeId)
                )
            }
        }
    }
}