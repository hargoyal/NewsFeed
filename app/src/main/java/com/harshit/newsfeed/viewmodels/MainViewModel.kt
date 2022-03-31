package com.harshit.newsfeed.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harshit.newsfeed.Constants
import com.harshit.newsfeed.interfaces.ApiInterface
import com.harshit.newsfeed.models.NewsFeedModel
import com.harshit.newsfeed.services.RetrofitService
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {

    private var newsFeedList:MutableLiveData<List<NewsFeedModel.Article>>? = null
    private var pageCompleted = false
    private var pageCount = 1
    fun getNewsFeedData(): LiveData<List<NewsFeedModel.Article>>{
        if (newsFeedList == null){
            newsFeedList = MutableLiveData<List<NewsFeedModel.Article>>()

            getNewsFeedList("$pageCount")
        } else {
            if(!pageCompleted) {
                pageCount += 1
                getNewsFeedList(pageCount.toString())
            }
        }

        return newsFeedList as MutableLiveData<List<NewsFeedModel.Article>>
    }

    private fun getNewsFeedList(page:String) {
        val clientInstance = RetrofitService.createService(ApiInterface::class.java)
        val call = clientInstance.getNewsFeed(Constants.domain,page,Constants.apiKey)
        call.enqueue(object : retrofit2.Callback<NewsFeedModel> {
            override fun onFailure(call: Call<NewsFeedModel>?, t: Throwable?) {
                pageCompleted = true
            }

            override fun onResponse(call: Call<NewsFeedModel>?, response: Response<NewsFeedModel>?) {
                if (response!!.isSuccessful){
                    newsFeedList?.value = response.body()?.articles
                    if(newsFeedList?.value?.size == response.body()?.totalResults) pageCompleted = true
//                    Page limitation in newsApi developer account to 100 records.
                } else {
                    pageCompleted = true
                }
            }

        })
    }
}