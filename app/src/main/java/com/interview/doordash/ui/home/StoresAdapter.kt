package com.interview.doordash.ui.home

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.interview.doordash.R
import com.interview.doordash.data.Store
import com.interview.doordash.data.StoreStatus
import com.interview.doordash.databinding.RowStoreLayoutBinding

class StoresAdapter(private val onStoreClicked: (Store) -> Unit) : RecyclerView.Adapter<StoreViewHolder>() {
    private val stores: MutableList<Store> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return if (viewType == VIEW_TYPE_LOADING) {
            val view = layoutInflater.inflate(R.layout.row_loading_spinner, parent, false)
            LoadingStoreViewHolder(view)
        } else {
            val binding = RowStoreLayoutBinding.inflate(layoutInflater, parent, false)
            DataStoreViewHolder(binding, onStoreClicked)
        }
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        (holder as? DataStoreViewHolder)?.bind(stores[position])
    }

    override fun getItemCount(): Int = stores.size

    override fun getItemViewType(position: Int): Int {
        return if (stores[position] != EMPTY_STORE) VIEW_TYPE_ITEM else VIEW_TYPE_LOADING
    }

    fun addStores(storesToAdd: List<Store>) {
        val insertIndex = stores.size
        stores.addAll(storesToAdd)
        notifyItemRangeInserted(insertIndex, storesToAdd.size)
    }

    fun addLoadingView() {
        Handler().post {
            if (stores.contains(EMPTY_STORE)) return@post
            stores.add(EMPTY_STORE)
            notifyItemInserted(stores.size - 1)
        }
    }

    fun removeLoadingView() {
        if (stores.size > 0 && stores[stores.size - 1] == EMPTY_STORE) {
            stores.removeAt(stores.size - 1)
            notifyItemRemoved(stores.size)
        }
    }

    companion object {
        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_LOADING = 2

        val EMPTY_STORE by lazy {
            val storeStatus = StoreStatus("", false, false, false, IntArray(0))
            Store(
                "",
                "",
                "",
                "",
                storeStatus,
                false,
                -1,
                -1.0,
                -1.0,
                -1,
                null
            )
        }
    }
}

sealed class StoreViewHolder(view: View) : RecyclerView.ViewHolder(view)

class LoadingStoreViewHolder(view: View) : StoreViewHolder(view)

class DataStoreViewHolder(
    private val binding: RowStoreLayoutBinding,
    private val onStoreClicked: (Store) -> Unit
) : StoreViewHolder(binding.root) {
    fun bind(store: Store) {
        binding.root.setOnClickListener { onStoreClicked(store) }
        binding.storeName.text = store.name
        binding.storeDescription.text = store.description

        val timeFromStore = store.status.getTimeFromStore(binding.root.context)
        binding.timeFromStore.text = timeFromStore

        Glide.with(binding.root)
            .load(store.restaurantImageUrl)
            .centerCrop()
            .into(binding.storeImage)

        binding.root.contentDescription = if (store.status.isAvailable) {
            binding.root.context.getString(R.string.store_available_row_content_description, store.name, store.description, timeFromStore)
        } else {
            binding.root.context.getString(R.string.store_closed_row_content_description, store.name, store.description)
        }
    }
}