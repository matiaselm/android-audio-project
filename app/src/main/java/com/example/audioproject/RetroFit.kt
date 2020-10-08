package com.example.audioproject

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.audioproject.Tag.TAG
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import java.io.IOException
import java.io.Serializable
import java.net.URL

object DemoApi {
    private const val URL = "https://freesound.org/"

    /**
     * Token for freesound.org API
     */
    const val token = "TwC9eGABRWKuCNfmh7L0fd0mZbJSX0TlnXTu1NzX"

    /**
     * Model for getting an object out of retrofit fetch result
     */
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

        data class Sound(
    val duration: Double,
    val images: Images,
    val name: String,
    val previews: Previews,
    val username: String
) : Serializable

data class Images(
    val waveform_m: String
) : Serializable

data class Previews(
    @SerializedName("preview-hq-mp3")
    val preview_hq_mp3: String,

) : Serializable
    }
/**
 * retrofit get calls with both text search query and getting more data on a single id
 */
    interface Service {


        //TODO limit search results
        @GET("apiv2/search/text/")
        suspend fun getSounds(
            @Query("query") query: String,
            @Query("filter") filter: String,
            @Query("token") token: String
        ): Model.Search

        @GET("apiv2/sounds/{Id}")
        suspend fun getSound(
            @Path("Id") id: String,
            @Query("token") token: String
        ): Model.Sound
    }

    /**
     * logginginterceptor and okHttpClient Log the api call in Logcat
     */
    private val loggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

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

/**
 * WebServiceRepository holds the methods for searching with retrofit
 */
class WebServiceRepository {
    private val call = DemoApi.service

    /**
     * getSounds fetches sounds from freesound with a search word and filters the result into a single user
     * @param query search word
     * @return
     */
    suspend fun getSounds(query: String): DemoApi.Model.Search? {
        return try {
            call.getSounds(query, "username:Ambientsoundapp", DemoApi.token)
        } catch (e: IOException) {
            Log.d(TAG, "$e")
            null
        }
    }

    /**
     * gets more information on a single sound result with id
     * @param id
     * @return
     */
    suspend fun getSound(id: String): DemoApi.Model.Sound? {
        return try {
            call.getSound(id, DemoApi.token)
        }catch(e: IOException){
            Log.d(TAG,"$e")
            null
        }
    }
}

/**
 * ViewModel for holding livedata from the GET queries
 */
class MainViewModel : ViewModel() {
    private val repository: WebServiceRepository = WebServiceRepository()
    val query = MutableLiveData<String>()
    fun queryWithText(text: String) {
        query.value = text
    }
    val results = query.switchMap {
        liveData(Dispatchers.IO) {
            emit(repository.getSounds(it))
        }
    }
}