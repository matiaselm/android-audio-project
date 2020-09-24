package com.example.audioproject

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.audioproject.Tag.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity(), OnResultSelected {
    lateinit var viewModel: MainViewModel

    @ExperimentalCoroutinesApi
    private fun getSound(id: Int) {
        try {
            var result: DemoApi.Model.Sound? = null

            Log.d(TAG, "getSound id: $id")

            lifecycleScope.launch(Dispatchers.IO) {

                result = WebServiceRepository().getSound(id.toString())

                Log.d(TAG, "lifecycleScope, result: $result")
                if (result != null) {
                    val soundUrl: URL = result!!.previews[2] // High quality mp3
                    Log.d(TAG, "soundUrl: $soundUrl")
                    val play = async(Dispatchers.IO) {
                        playAudio(soundUrl, result!!.name)
                    }

                    play.await()
                } else {
                    Log.d(TAG, "result = null, $result")
                }
            }
        } catch (e: IOException) {
            Log.d(TAG, "error in getSound: $e")
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

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, SearchListFragment.newInstance())
                .commit()
        }
    }

    override fun onClickResult(result: DemoApi.Model.Result, position: Int) {
        Log.d(TAG, result.id.toString() + "add")
    }

    @ExperimentalCoroutinesApi
    override fun onClickPlay(result: DemoApi.Model.Result, position: Int) {
        Log.d(TAG, result.id.toString() + "play")
        // getSound(result.id) // This is the thing that adds functionality to play selected sound
    }
}