package com.interview.doordash

import android.os.Build
import android.os.Looper.getMainLooper
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.interview.doordash.data.Store
import com.interview.doordash.data.StoreStatus
import com.interview.doordash.databinding.RowStoreLayoutBinding
import com.interview.doordash.ui.home.DataStoreViewHolder
import com.interview.doordash.ui.home.LoadingStoreViewHolder
import com.interview.doordash.ui.home.StoresAdapter
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class StoresAdapterTest {

    private val storeName = "Panda Express"
    private val description = "Chinese"

    private val storeStatus = StoreStatus(
        unavailableReason = "",
        isPickupAvailable = true,
        isAsapAvailable = false,
        isScheduledAvailable = false,
        intArrayOf(22, 22)
    )
    private val store = Store(
        id = "1",
        storeName,
        description,
        restaurantImageUrl = "",
        storeStatus,
        isSubscriptionEligible = true,
        numberOfRatings = 100,
        averageRating = 4.5,
        distanceToStore = 0.5,
        priceRange = 2,
        menus = null
    )

    @Before
    fun setup() {
        shadowOf(getMainLooper()).idle()
    }

    @After
    fun clean() {
        shadowOf(getMainLooper()).idle()
    }

    @Test
    fun `Test that onCreateViewHolder returns LoadingStoreViewHolder instance for viewType VIEW_TYPE_LOADING`() {
        val adapter = StoresAdapter {}

        val parent = LinearLayout(ApplicationProvider.getApplicationContext())

        val viewHolder = adapter.onCreateViewHolder(parent, StoresAdapter.VIEW_TYPE_LOADING)
        Truth.assertThat(viewHolder).isInstanceOf(LoadingStoreViewHolder::class.java)
    }

    @Test
    fun `Test that onCreateViewHolder returns DataStoreViewHolder instance for viewType VIEW_TYPE_ITEM`() {
        val adapter = StoresAdapter {}

        val parent = LinearLayout(ApplicationProvider.getApplicationContext())

        val viewHolder = adapter.onCreateViewHolder(parent, StoresAdapter.VIEW_TYPE_ITEM)
        Truth.assertThat(viewHolder).isInstanceOf(DataStoreViewHolder::class.java)
    }

    @Test
    fun `Test that the adapter itemCount changes after adding stores`() {
        val adapter = StoresAdapter {}

        Truth.assertThat(adapter.itemCount).isEqualTo(0)

        val mockStore1: Store = mockk()
        val mockStore2: Store = mockk()
        val mockStore3: Store = mockk()

        adapter.addStores(listOf(mockStore1, mockStore2, mockStore3))

        Truth.assertThat(adapter.itemCount).isEqualTo(3)
    }

    @Test
    fun `Test that all the values are properly set in the ViewHolder from the Store`() {
        val inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
        val parent = LinearLayout(ApplicationProvider.getApplicationContext())
        val binding = RowStoreLayoutBinding.inflate(inflater, parent, false)

        val onStoreClicked: (Store) -> Unit = {}

        val viewHolder = DataStoreViewHolder(binding, onStoreClicked)

        viewHolder.bind(store)

        Truth.assertThat(binding.storeName.text).isEqualTo(storeName)
        Truth.assertThat(binding.storeDescription.text).isEqualTo(description)

        val timeFromStore = store.status.getTimeFromStore(binding.root.context)
        Truth.assertThat(binding.timeFromStore.text).isEqualTo(timeFromStore)
    }

    @Test
    fun `Test that the lambda is called when the root is clicked`() {
        val inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
        val parent = LinearLayout(ApplicationProvider.getApplicationContext())
        val binding = RowStoreLayoutBinding.inflate(inflater, parent, false)

        val onStoreClicked: (Store) -> Unit = spyk()

        val viewHolder = DataStoreViewHolder(binding, onStoreClicked)

        viewHolder.bind(store)

        binding.root.performClick()

        verify(exactly = 1) { onStoreClicked(store) }
    }
}