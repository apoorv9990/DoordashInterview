package com.interview.doordash.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interview.doordash.databinding.FragmentStoresBinding
import com.interview.doordash.network.RetrofitBuilder
import com.interview.doordash.repository.home.StoreRepository
import com.interview.doordash.utils.EndlessRecyclerViewScrollListener

class StoresFragment : Fragment() {
    private val storeRepository: StoreRepository = StoreRepository(RetrofitBuilder.apiService)

    private val storesViewModel: StoresViewModel by viewModels(factoryProducer = {
         StoresViewModelFactory(storeRepository)
    })

    private var fragmentStoresBinding: FragmentStoresBinding? = null

    private val storesAdapter by lazy {
        StoresAdapter {
            val action = StoresFragmentDirections.actionStoresFragmentToStoreDetailFragment(it)
            findNavController().navigate(action)
        }
    }

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentStoresBinding = FragmentStoresBinding.inflate(inflater, container, false)

        fragmentStoresBinding?.let {
            val linearLayoutManager = LinearLayoutManager(it.root.context)
            it.storesRecyclerView.layoutManager = linearLayoutManager
            it.storesRecyclerView.adapter = storesAdapter

            scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    storesViewModel.fetchStores(page)
                }
            }

            it.storesRecyclerView.addOnScrollListener(scrollListener)
        }

        storesViewModel.storesLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is GetStoresLoading -> handleLoadingState()
                is GetStoresError -> {
                    // show error state if needed
                }
                is GetStoresSuccess -> {
                    handleSuccessState()
                    storesAdapter.removeLoadingView()
                    storesAdapter.addStores(result.stores)
                }
            }
        })
        storesViewModel.fetchStores(0)

        return fragmentStoresBinding!!.root
    }

    private fun handleLoadingState() {
        fragmentStoresBinding?.let {
            if (storesAdapter.itemCount == 0) {
                it.loadingProgressbar.visibility = View.VISIBLE
                it.storesRecyclerView.visibility = View.GONE
            } else {
                storesAdapter.addLoadingView()
            }
        }
    }

    private fun handleSuccessState() {
        fragmentStoresBinding?.let {
            it.loadingProgressbar.visibility = View.GONE
            it.storesRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentStoresBinding = null
    }
}