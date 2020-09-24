package com.example.audioproject

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity(), OnResultSelected {
    lateinit var viewModel: MainViewModel

    val TAG = "sound-app"

    @ExperimentalCoroutinesApi
    private fun getSound(id: Int) {
        val repository = WebServiceRepository()
        var result: DemoApi.Model.Sound? = null

        Log.d(TAG, "getSound id: $id")

        lifecycleScope.launch(Dispatchers.IO) {
            result = repository.getSound(id.toString())

            Log.d(TAG,"lifecycleScope, result: $result")
            if (result != null) {
                val soundUrl: URL = result!!.previews[2] // High quality mp3
                Log.d(TAG,"soundUrl: $soundUrl")
                val play = async(Dispatchers.IO){
                    playAudio(soundUrl, result!!.name)
                }

                play.await()
            }
        }
    }


    private fun playAudio(track: URL, audioName: String) {
        val mediaPlayer1: MediaPlayer? = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnCompletionListener { }
            setDataSource(track.toString())
            prepare()
            start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, SearchListFragment.newInstance())
                .commit()
        }

        //TODO Polish your bolete or something
        //TODO alot of stuff really.
    }

    override fun onClickResult(result: DemoApi.Model.Result, position: Int) {
        Log.d("onClick", result.id.toString() + "add")
    }

    @ExperimentalCoroutinesApi
    override fun onClickPlay(result: DemoApi.Model.Result, position: Int) {
        Log.d("onClick", result.id.toString() + "play")
        getSound(result.id)
    }
}