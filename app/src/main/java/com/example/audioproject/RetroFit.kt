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
    const val token = "TwC9eGABRWKuCNfmh7L0fd0mZbJSX0TlnXTu1NzX"
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
        @GET("apiv2/search/text/")
        suspend fun getSounds(@Query("query") query: String,
                              @Query("token") token: String
        ) : Model.Search
    }

    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build()
    val service = retrofit.create(Service::class.java)
}

class WebServiceRepository(){

    private val call = DemoApi.service
    suspend fun getSounds(query: String): DemoApi.Model.Search{return call.getSounds(query, DemoApi.token) }
}

class MainViewModel: ViewModel() {
    private val repository: WebServiceRepository = WebServiceRepository()
    val query = MutableLiveData<String>()
    fun queryWithText(text: String) {query.value = text
    }
    val results = query.switchMap {
        liveData(Dispatchers.IO) {
            emit(repository.getSounds(it))}
    }
}