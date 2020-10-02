package com.example.audioproject.mySoundscapes

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.audioproject.DemoApi
import com.example.audioproject.R
import com.example.audioproject.Soundscape
import com.example.audioproject.Soundscapes
import com.example.audioproject.Soundscapes.soundscapes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/* TODO: mySoundScapes-activity's fragments/ functionality
        -> list of soundscapes with the functionality to play whatever of them the user wants
    */

class MySoundscapesActivity : AppCompatActivity(), OnSoundscapeSelected {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_soundscape)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mySoundscapeContainer, MySoundscapesFragment.newInstance())
                .commit()
        }
    }

    private fun playAudio(sounds: ArrayList<DemoApi.Model.Sound>) {
        Log.d("ss", sounds.toString())
        val sourceList = ArrayList<String>()
        for (sound in sounds) {
            sourceList.add(sound.previews.preview_hq_mp3)
        }

        Log.d("source", sourceList.toString())
        for (source in sourceList) {
            lateinit var mp: MediaPlayer
            lifecycleScope.launch(Dispatchers.IO) {
                val play = async(Dispatchers.IO) {
                    mp = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )

                        setOnCompletionListener {
                            Toast.makeText(
                                this@MySoundscapesActivity,
                                "Finished playing soundscape",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        setDataSource(source)
                        prepare()
                    }
                }
                play.await()
                mp.start()
            }
        }
    }

    override fun onSelect(result: String, position: Int) {
        // TODO: Add functionality
    }

    override fun onPlay(soundscape: Soundscape, position: Int) {
        Log.d("ss", soundscape.ssSounds.toString() + "clicked")
        Log.d("ss", soundscape.name)
        Log.d("ss", soundscapes[0].ssSounds.toString())
        playAudio(soundscape.ssSounds)
    }
}