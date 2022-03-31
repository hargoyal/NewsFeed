package com.harshit.newsfeed.interfaces

import com.harshit.newsfeed.models.NewsFeedModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("v2/everything")
    fun getNewsFeed(
        @Query("domains") domains:String,
        @Query("page") page:String,
        @Query("apiKey") apiKey:String
    ): Call<NewsFeedModel>
}