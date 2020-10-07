package com.example.audioproject.allSounds

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.audioproject.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class AllSoundsActivity : AppCompatActivity(), OnResultSelected {
    private fun playAudio(id: Int, playButton: Button) {
        var result: DemoApi.Model.Sound? = null

        playButton.text = getString(R.string.playing)
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
                            Toast.makeText(
                                this@AllSoundsActivity,
                                "Finished playing: $soundName",
                                Toast.LENGTH_SHORT
                            ).show()
                            playButton.text = getString(R.string.play)
                        }

                        setDataSource(soundUrl.toString())
                        prepare()
                        start()
                    }
                }

                play.await()
            } else {
                Log.d(Tag.TAG, "result = null, $result")
                playButton.text = getString(R.string.play)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_sounds)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.allSoundsContainer, SearchListFragment.newInstance())
                .commit()
        }
    }

    override fun onClickResult(result: DemoApi.Model.Result, position: Int) {
        Log.d(Tag.TAG, result.id.toString() + "add")
    }

    @ExperimentalCoroutinesApi
    override fun onClickPlay(result: DemoApi.Model.Result, position: Int, playButton: Button) {
        Log.d(Tag.TAG, result.id.toString() + "play")
        playAudio(
            result.id,
            playButton
        ) // This is the thing that is supposed to add functionality to play selected sound_list_item
    }
}