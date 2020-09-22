package com.example.audioproject

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


object DemoApi {

    private const val URL = "https://freesound.org/"
    //token is from registering in freesound.org
    const val token = "TwC9eGABRWKuCNfmh7L0fd0mZbJSX0TlnXTu1NzX"

    //data structure from json
    object Model {
        data class Search(
            val count: Int,
            val next: String,
            val previous: Any,
            val results: List<Result>
        )

        data class Result(
            val id: Int,
            val license: String,
            val name: String,
            val tags: List<String>,
            val username: String
        )
    }
    interface Service {
        //get api call with text search, might have to change this
        @GET("apiv2/search/text/")
        suspend fun getSounds(@Query("query") query: String,
                              @Query("token") token: String
        ) : Model.Search
    }
        //logginginterceptor and okHttpClient Log the api call in Logcat
        // you can easily see from which url youre trying to get data from
        //google setLevel method to get more or less data from apicall
    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    //Http client put to retrofit builder as client
    private val retrofit = Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build()
    val service = retrofit.create(Service::class.java)
}

class WebServiceRepository(){
    private val call = DemoApi.service
    //call this to start a GET request in mainactivity, takes in a search word and the api key token is constant
    suspend fun getSounds(query: String): DemoApi.Model.Search {
        return call.getSounds(query, DemoApi.token)
    }
}

class MainViewModel: ViewModel() {
    private val repository: WebServiceRepository = WebServiceRepository()
    // query is livedata of strings so when you change the search word it will call new get request
    val query = MutableLiveData<String>()
    fun queryWithText(text: String) {query.value = text
    }
    //switchmap function turns the thing inside into a livedata, in this case repository.getSounds(it) which returns a
    //DemoApi.Model.Search object
    val results = query.switchMap {
        liveData(Dispatchers.IO) {
            emit(repository.getSounds(it))}
    }
}