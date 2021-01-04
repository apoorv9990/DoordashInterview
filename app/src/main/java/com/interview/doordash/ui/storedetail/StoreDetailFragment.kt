package com.interview.doordash.ui.storedetail

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.interview.doordash.R
import com.interview.doordash.data.Store
import com.interview.doordash.databinding.FragmentStoreDetailBinding

class StoreDetailFragment : Fragment() {
    private val args: StoreDetailFragmentArgs by navArgs()

    private var fragmentStoreDetailBinding: FragmentStoreDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentStoreDetailBinding = FragmentStoreDetailBinding.inflate(inflater, container, false)

        val store = args.store

        fragmentStoreDetailBinding?.let {
            it.storeName.text = store.name

            setStoreDescription(store, it.storeDescription)
            setStoreStats(store, it.storeStats)

            it.popularItemsRecyclerView.apply {
                val popularItems = store.menus?.get(0)?.popularItems
                layoutManager = LinearLayoutManager(requireContext())
                adapter = PopularMenuAdapter(popularItems ?: listOf())
            }
        }

        return fragmentStoreDetailBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentStoreDetailBinding = null
    }

    private fun setStoreDescription(store: Store, storeDescription: TextView) {
        val spannableStringBuilder = SpannableStringBuilder()

        if (store.isSubscriptionEligible) {
            val dashPass = "Dashpass"
            spannableStringBuilder.append(dashPass)

            val foregroundColor = ContextCompat.getColor(requireContext(), R.color.teal_700)
            val foregroundColorSpan = ForegroundColorSpan(foregroundColor)
            spannableStringBuilder.setSpan(
                foregroundColorSpan,
                0,
                dashPass.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannableStringBuilder.append(SPACE_UNICODE)
            spannableStringBuilder.append(BULLET_UNICODE)
            spannableStringBuilder.append(SPACE_UNICODE)
        }

        store.description.split(",").take(3).forEachIndexed { index, s ->
            spannableStringBuilder.append(s)
            if (index != 2) {
                spannableStringBuilder.append(SPACE_UNICODE)
                spannableStringBuilder.append(BULLET_UNICODE)
            }
        }

        storeDescription.text = spannableStringBuilder
    }

    private fun setStoreStats(store: Store, storeStats: TextView) {
        SpannableStringBuilder().apply {
            append(store.averageRating.toString())
            append(SPACE_UNICODE)
            append(STAR_UNICODE)
            append(SPACE_UNICODE)
            append(store.numberOfRatings.toString())
            append(SPACE_UNICODE)
            append("ratings")
            append(SPACE_UNICODE)
            append(BULLET_UNICODE)
            append(SPACE_UNICODE)
            append(String.format("%.2f", store.distanceToStore))
            append(SPACE_UNICODE)
            append("mi")
            append(SPACE_UNICODE)
            append(BULLET_UNICODE)
            append(SPACE_UNICODE)

            for (i in 0 until store.priceRange) append("$")

            storeStats.text = this
        }
    }

    companion object {
        private const val SPACE_UNICODE = "\u0020"
        private const val BULLET_UNICODE = "\u2022"
        private const val STAR_UNICODE = "\u2605"
    }
}