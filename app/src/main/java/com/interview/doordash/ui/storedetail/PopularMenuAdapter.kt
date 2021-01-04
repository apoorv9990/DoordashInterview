package com.interview.doordash.ui.storedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.interview.doordash.data.MenuItem
import com.interview.doordash.databinding.RowPopularItemBinding
import java.text.NumberFormat

class PopularMenuAdapter(private val popularMenuItems: List<MenuItem>) : RecyclerView.Adapter<PopularMenuViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMenuViewHolder {
        val context = LayoutInflater.from(parent.context)
        val binding = RowPopularItemBinding.inflate(context, parent, false)
        return PopularMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularMenuViewHolder, position: Int) {
        holder.bind(popularMenuItems[position])
    }

    override fun getItemCount(): Int = popularMenuItems.size
}

class PopularMenuViewHolder(private val binding: RowPopularItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(popularMenuItem: MenuItem) {
        binding.popularMenuItemName.text = popularMenuItem.name
        binding.popularMenuItemPrice.text = NumberFormat.getCurrencyInstance().format(popularMenuItem.price/100)

        Glide.with(binding.root)
            .load(popularMenuItem.menuImage)
            .fitCenter()
            .into(binding.popularMenuItemImage)
    }
}