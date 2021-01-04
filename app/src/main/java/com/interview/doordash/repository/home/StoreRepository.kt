package com.interview.doordash.repository.home

import com.interview.doordash.network.ApiService

class StoreRepository(private val apiService: ApiService) {

    // If we ever want to implement caching we can implement
    // it here to return data locally if available
    suspend fun getStores(
        latitude: String,
        longitude: String,
        offset: Int,
        limit: Int
    ) = apiService.getStoreFeed(
        latitude,
        longitude,
        offset.toString(),
        limit.toString()
    ).stores
}