package com.interview.doordash.network

import com.interview.doordash.data.StoreFeed
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/v1/store_feed/")
    suspend fun getStoreFeed(
        @Query("lat") latitude: String,
        @Query("lng") longitude: String,
        @Query("offset") offset: String,
        @Query("limit") limit: String
    ): StoreFeed
}