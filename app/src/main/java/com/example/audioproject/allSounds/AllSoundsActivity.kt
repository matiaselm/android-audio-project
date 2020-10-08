package com.example.audioproject.allSounds

import android.graphics.Paint
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.audioproject.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

/**
 * activity for looking at all sounds in the app
 */
class AllSoundsActivity : AppCompatActivity(), OnResultSelected {

    /**
     * fetches sounds data with id from freesound and plays it
     * @param id id of a sound to search from freesound
     * @param button to disable it while playing sounds so you cant play multiple sounds at the same time
     */
    private fun playAudio(id: Int, playButton: Button) {
        var result: DemoApi.Model.Sound? = null

        playButton.isEnabled = false
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
                            playButton.isEnabled = true
                            Toast.makeText(
                                this@AllSoundsActivity,
                                "${getString(R.string.finished_playing)} $soundName",
                                Toast.LENGTH_SHORT
                            ).show()
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

        toolbar.topAppBar.title = getString(R.string.activity_all_sounds)
        toolbar.topAppBar.setBackgroundColor(getColor(R.color.colorSecondaryGlass))

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.allSoundsContainer, SearchListFragment.newInstance())
                .commit()
        }
    }
    /**
     * onclick method
     * calls playaudio to play the given sound
     * @see playAudio
     * @param result has the id to search for the sound info
     * @param playButton
     * @param position
     */
    @ExperimentalCoroutinesApi
    override fun onClickPlay(result: DemoApi.Model.Result, position: Int, playButton: Button) {
        Log.d(Tag.TAG, result.id.toString() + "play")
        playAudio(
            result.id,
            playButton
        )
    }
}