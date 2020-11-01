package com.ttenushko.mvp.demo.presentation.screens.home.addplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ttenushko.mvp.MvpViewModel
import com.ttenushko.mvp.demo.R
import com.ttenushko.mvp.demo.domain.weather.model.Place
import com.ttenushko.mvp.demo.presentation.base.error.DefaultErrorHandler
import com.ttenushko.mvp.demo.presentation.base.fragment.BaseMvpFragment
import com.ttenushko.mvp.demo.presentation.di.utils.findComponentDependencies
import com.ttenushko.mvp.demo.presentation.screens.home.addplace.di.AddPlaceFragmentModule
import com.ttenushko.mvp.demo.presentation.screens.home.addplace.di.DaggerAddPlaceFragmentComponent
import com.ttenushko.mvp.demo.presentation.screens.home.common.PlaceAdapter
import com.ttenushko.mvp.demo.presentation.utils.isVisible
import kotlinx.android.synthetic.main.fragment_add_place.*
import kotlinx.android.synthetic.main.toolbar_with_search.*
import javax.inject.Inject


class AddPlaceFragment :
    BaseMvpFragment<AddPlaceContract.View, AddPlaceContract.Presenter.State, AddPlaceContract.Presenter>() {

    companion object {
        private const val ARG_SEARCH = "search"
        fun args(search: String): Bundle =
            Bundle().apply {
                putString(ARG_SEARCH, search)
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var placeAdapter: PlaceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAddPlaceFragmentComponent.builder()
            .addPlaceFragmentDependencies(findComponentDependencies())
            .addPlaceFragmentModule(AddPlaceFragmentModule(arguments!!.getString(ARG_SEARCH)!!))
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_add_place, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        placeAdapter = PlaceAdapter(
            context!!,
            object :
                PlaceAdapter.Callback {
                override fun onItemClicked(place: Place) {
                    presenter.placeClicked(place)
                }
            })
        placeList.apply {
            adapter = placeAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        searchView.apply {
            isFocusable = true
            isIconified = false
            setOnQueryTextListener(searchTextWatcher)
            setIconifiedByDefault(false)
            requestFocusFromTouch()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
        placeAdapter = null
    }

    override fun onStart() {
        super.onStart()
        toolbar?.title = "Add Place"
    }

    override fun getMvpView(): AddPlaceContract.View =
        view

    override fun getMvpViewModel(): MvpViewModel<AddPlaceContract.View, AddPlaceContract.Presenter.State, AddPlaceContract.Presenter> =
        ViewModelProviders.of(this, viewModelFactory)[AddPlaceViewModel::class.java]

    private val searchTextWatcher = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(text: String): Boolean {
            return false
        }

        override fun onQueryTextChange(text: String): Boolean {
            presenter.searchChanged(text)
            return true
        }
    }

    private val view = object : AddPlaceContract.View {
        override fun setSearch(search: String) {
            if (search != searchView.query.toString()) {
                searchView.setOnQueryTextListener(null)
                searchView.setQuery(search, true)
                searchView.setOnQueryTextListener(searchTextWatcher)
            }
        }

        override fun setSearching(isSearching: Boolean) {
            progress.isVisible = isSearching
        }

        override fun showSearchPrompt(prompt: AddPlaceContract.SearchPrompt) {
            when (prompt) {
                AddPlaceContract.SearchPrompt.BLANK -> {
                    message.isVisible = true
                    message.text = "Start typing text to search"
                }
                AddPlaceContract.SearchPrompt.NO_RESULTS -> {
                    message.isVisible = true
                    message.text = "Nothing found"
                }
                AddPlaceContract.SearchPrompt.ERROR -> {
                    message.isVisible = true
                    message.text = "Error occurred"
                }
                else -> {
                    message.isVisible = false
                }
            }
        }

        override fun setPlaces(places: List<Place>) {
            placeAdapter!!.set(places)
        }

        override fun showErrorPopup(error: Throwable) {
            DefaultErrorHandler.showError(this@AddPlaceFragment, null, error)
        }
    }
}