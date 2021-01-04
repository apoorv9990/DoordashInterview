package com.interview.doordash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.interview.doordash.data.Store
import com.interview.doordash.repository.home.StoreRepository
import com.interview.doordash.ui.home.GetStoresError
import com.interview.doordash.ui.home.GetStoresSuccess
import com.interview.doordash.ui.home.StoresViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class StoresViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Test liveData sends out GetStoresError when error is thrown while fetching data from repository`() {
        val storeRepository: StoreRepository = mockk()
        coEvery { storeRepository.getStores(any(), any(), any(), any()) } throws RuntimeException()

        val viewModel = StoresViewModel(storeRepository)

        viewModel.storesLiveData.observeForever {}

        viewModel.fetchStores(0)

        Truth.assertThat(viewModel.storesLiveData.value).isEqualTo(GetStoresError)
    }

    @Test
    fun `Test liveData sends out GetStoresSuccess when repository returns proper values`() {
        val storeRepository: StoreRepository = mockk()

        val mockStore1: Store = mockk()
        val mockStore2: Store = mockk()
        val mockStore3: Store = mockk()

        val stores = listOf(mockStore1, mockStore2, mockStore3)

        coEvery { storeRepository.getStores(any(), any(), any(), any()) } returns stores

        val viewModel = StoresViewModel(storeRepository)

        viewModel.storesLiveData.observeForever {}

        viewModel.fetchStores(0)

        Truth.assertThat(viewModel.storesLiveData.value).isInstanceOf(GetStoresSuccess::class.java)
        Truth.assertThat((viewModel.storesLiveData.value as GetStoresSuccess).stores).isEqualTo(stores)
    }
}
