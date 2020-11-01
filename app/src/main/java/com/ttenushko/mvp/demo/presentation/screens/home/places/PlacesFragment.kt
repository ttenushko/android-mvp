package com.ttenushko.mvp.demo.presentation.screens.home.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ttenushko.mvp.MvpViewModel
import com.ttenushko.mvp.demo.R
import com.ttenushko.mvp.demo.domain.weather.model.Place
import com.ttenushko.mvp.demo.presentation.base.error.DefaultErrorHandler
import com.ttenushko.mvp.demo.presentation.base.fragment.BaseMvpFragment
import com.ttenushko.mvp.demo.presentation.di.utils.findComponentDependencies
import com.ttenushko.mvp.demo.presentation.screens.home.common.PlaceAdapter
import com.ttenushko.mvp.demo.presentation.screens.home.places.di.DaggerPlacesFragmentComponent
import com.ttenushko.mvp.demo.presentation.utils.isVisible
import kotlinx.android.synthetic.main.fragment_places.*
import kotlinx.android.synthetic.main.layout_places_content.*
import javax.inject.Inject

class PlacesFragment :
    BaseMvpFragment<PlacesContract.View, PlacesContract.Presenter.State, PlacesContract.Presenter>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var placeAdapter: PlaceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerPlacesFragmentComponent.builder()
            .placesFragmentDependencies(findComponentDependencies())
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_places, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAddPlace.setOnClickListener { presenter.addPlaceButtonClicked() }
        placeAdapter = PlaceAdapter(
            context!!,
            object :
                PlaceAdapter.Callback {
                override fun onItemClicked(place: Place) {
                    presenter.placeClicked(place)
                }
            })
        placeList.adapter = placeAdapter
        placeList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        placeAdapter = null
    }

    override fun onStart() {
        super.onStart()
        toolbar?.title = "Places"
    }

    override fun getMvpView(): PlacesContract.View =
        view

    override fun getMvpViewModel(): MvpViewModel<PlacesContract.View, PlacesContract.Presenter.State, PlacesContract.Presenter> =
        ViewModelProviders.of(this, viewModelFactory)[PlacesViewModel::class.java]

    private val view = object : PlacesContract.View {
        override fun setPlaces(places: List<Place>?, error: Throwable?) {
            layoutContent.isVisible = (null != places)
            layoutContentFilled.isVisible = (null != places && places.isNotEmpty())
            layoutContentEmpty.isVisible = (null != places && places.isEmpty())
            layoutError.isVisible = (null != error)
            if (null != places) {
                placeAdapter!!.set(places)
            } else {
                placeAdapter!!.clear()
            }
        }

        override fun setLoading(isLoading: Boolean) {
            layoutLoading.isVisible = isLoading
        }

        override fun showErrorPopup(error: Throwable) {
            DefaultErrorHandler.showError(this@PlacesFragment, null, error)
        }
    }
}
