package com.example.audioproject

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.example.audioproject.Tag.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.recycler_item_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity(), OnResultSelected {
    private val context = this

    private fun playAudio(id: Int) {
        var result: DemoApi.Model.Sound? = null

        Log.d(Tag.TAG, "playAudio id: $id")
        lifecycleScope.launch(Dispatchers.IO) {
            result = WebServiceRepository().getSound(id.toString())

            Log.d(Tag.TAG, "lifecycleScope, result: $result")

            if (result != null) {
                val soundUrl = URL(result!!.previews.preview_hq_mp3) // High quality mp3
                val soundName = result!!.name

                Log.d(Tag.TAG, "soundUrl: $soundUrl")
                val play = async(Dispatchers.IO) {
                    MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )

                        setOnCompletionListener {
                            Toast.makeText(context,"Finished playing: $soundName", Toast.LENGTH_SHORT).show()
                        }

                        setDataSource(soundUrl.toString())
                        prepare()
                        start()
                    }
                }

                play.await()
            } else {
                Log.d(Tag.TAG, "result = null, $result")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, NavigatorFragment.newInstance())
                .commit()
        }
    }

    override fun onClickResult(result: DemoApi.Model.Result, position: Int) {
        Log.d(TAG, result.id.toString() + "add")
    }

    @ExperimentalCoroutinesApi
    override fun onClickPlay(result: DemoApi.Model.Result, position: Int) {
        Log.d(TAG, result.id.toString() + "play")
        playAudio(result.id) // This is the thing that is supposed to add functionality to play selected sound
    }
}