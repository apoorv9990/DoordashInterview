package com.interview.doordash.ui.home

import androidx.lifecycle.*
import com.interview.doordash.data.Store
import com.interview.doordash.repository.home.StoreRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class StoresViewModel(private val storeRepository: StoreRepository) : ViewModel() {
    private val _storesLiveData: MutableLiveData<GetStoresResult> = MutableLiveData()
    val storesLiveData: LiveData<GetStoresResult> = _storesLiveData

    fun fetchStores(offset: Int) {
        viewModelScope.launch {
            try {
                _storesLiveData.value = GetStoresLoading
                val stores = storeRepository.getStores(LAT, LNG, (offset * LIMIT), LIMIT)
                _storesLiveData.value = GetStoresSuccess(stores)
            } catch (exception: Exception) {
                exception.printStackTrace()
                _storesLiveData.value = GetStoresError
            }
        }
    }

    companion object {
        private const val LAT = "37.422740"
        private const val LNG = "-122.139956"
        private const val LIMIT = 10
    }
}

class StoresViewModelFactory(private val storeRepository: StoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoresViewModel::class.java)) {
            return StoresViewModel(storeRepository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}

sealed class GetStoresResult
object GetStoresLoading : GetStoresResult()
data class GetStoresSuccess(val stores: List<Store>) : GetStoresResult()
object GetStoresError : GetStoresResult()