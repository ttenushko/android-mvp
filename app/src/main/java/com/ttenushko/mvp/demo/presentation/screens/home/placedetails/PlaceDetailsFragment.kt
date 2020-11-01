package com.ttenushko.mvp.demo.presentation.screens.home.placedetails

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.ttenushko.mvp.MvpViewModel
import com.ttenushko.mvp.demo.R
import com.ttenushko.mvp.demo.domain.weather.model.Weather
import com.ttenushko.mvp.demo.presentation.base.error.DefaultErrorHandler
import com.ttenushko.mvp.demo.presentation.base.fragment.BaseMvpFragment
import com.ttenushko.mvp.demo.presentation.di.utils.findComponentDependencies
import com.ttenushko.mvp.demo.presentation.dialogs.DialogFragmentClickListener
import com.ttenushko.mvp.demo.presentation.dialogs.SimpleDialogFragment
import com.ttenushko.mvp.demo.presentation.screens.home.placedetails.di.DaggerPlaceDetailsFragmentComponent
import com.ttenushko.mvp.demo.presentation.screens.home.placedetails.di.PlaceDetailsFragmentModule
import com.ttenushko.mvp.demo.presentation.utils.ValueUpdater
import com.ttenushko.mvp.demo.presentation.utils.isDialogShown
import com.ttenushko.mvp.demo.presentation.utils.showDialog
import kotlinx.android.synthetic.main.fragment_place_details.*
import javax.inject.Inject

class PlaceDetailsFragment :
    BaseMvpFragment<PlaceDetailsContract.View, PlaceDetailsContract.Presenter.State, PlaceDetailsContract.Presenter>(),
    DialogFragmentClickListener {

    companion object {
        private const val ARG_PLACE_ID = "placeId"
        private const val DLG_DELETE_CONFIRMATION = "deleteConfirmation"

        fun args(placeId: Long): Bundle =
            Bundle().apply {
                putLong(ARG_PLACE_ID, placeId)
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var picasso: Picasso
    private var menuItemDelete: MenuItem? = null
    private val menuItemDeleteVisibilityUpdater = ValueUpdater(false) { isVisible ->
        menuItemDelete?.isVisible = isVisible
    }
    private val weatherIconUrl = ValueUpdater("") { iconUrl ->
        if (iconUrl.isNotBlank()) {
            picasso.load(iconUrl).into(icon)
        } else {
            icon.setImageBitmap(null)
        }
    }

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerPlaceDetailsFragmentComponent.builder()
            .placeDetailsFragmentDependencies(findComponentDependencies())
            .placeDetailsFragmentModule(PlaceDetailsFragmentModule(arguments!!.getLong(ARG_PLACE_ID)))
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_place_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherIconUrl.set("")
        pullRefreshLayout.setOnRefreshListener { presenter.refreshClicked() }
    }

    override fun onStart() {
        super.onStart()
        toolbar?.title = "Place Details"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_place_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
        menuItemDelete = menu.findItem(R.id.delete)
        menuItemDeleteVisibilityUpdater.forceUpdate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.delete -> {
                presenter.deleteClicked()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onDialogFragmentClick(
        dialogFragment: DialogFragment,
        dialog: DialogInterface,
        which: Int
    ) {
        when (dialogFragment.tag) {
            DLG_DELETE_CONFIRMATION -> {
                if (DialogInterface.BUTTON_POSITIVE == which) {
                    presenter.deleteConfirmed()
                }
            }
        }
    }

    override fun getMvpView(): PlaceDetailsContract.View =
        view

    override fun getMvpViewModel(): MvpViewModel<PlaceDetailsContract.View, PlaceDetailsContract.Presenter.State, PlaceDetailsContract.Presenter> =
        ViewModelProviders.of(this, viewModelFactory)[PlaceDetailsViewModel::class.java]

    private val view = object : PlaceDetailsContract.View {
        override fun setWeather(weather: Weather?, error: Throwable?) {
            when {
                null != error -> {
                    layoutContent.visibility = View.GONE
                    layoutError.visibility = View.VISIBLE
                }
                null != weather -> {
                    layoutContent.visibility = View.VISIBLE
                    layoutError.visibility = View.GONE
                    place.text =
                        "${weather.place.name}, ${weather.place.countyCode.toUpperCase()}"
                    temperature.text = "${weather.conditions.tempCurrent.toInt()}\u2103"
                    temp_min.text = "${weather.conditions.tempMin.toInt()}\u2103"
                    temp_max.text = "${weather.conditions.tempMax.toInt()}\u2103"
                    humidity.text = "${weather.conditions.humidity}%"
                }
                else -> {
                    layoutContent.visibility = View.GONE
                    layoutError.visibility = View.GONE
                }
            }
            weatherIconUrl.set(weather?.descriptions?.firstOrNull()?.iconUrl ?: "")
        }

        override fun showDeleteConfirmation() {
            if (!childFragmentManager.isDialogShown(DLG_DELETE_CONFIRMATION)) {
                childFragmentManager.showDialog(
                    SimpleDialogFragment.newInstance(
                        null,
                        "Are you sure to remove this place?",
                        "OK",
                        "Cancel"
                    ), DLG_DELETE_CONFIRMATION
                )
            }
        }

        override fun setLoading(isLoading: Boolean) {
            pullRefreshLayout.isRefreshing = isLoading
        }

        override fun setDeleting(isDeleting: Boolean) {
            layoutProgress.visibility = if (isDeleting) View.VISIBLE else View.GONE
        }

        override fun enableDelete(enable: Boolean) {
            menuItemDeleteVisibilityUpdater.set(enable)
        }

        override fun showErrorPopup(error: Throwable) {
            DefaultErrorHandler.showError(this@PlaceDetailsFragment, null, error)
        }
    }
}