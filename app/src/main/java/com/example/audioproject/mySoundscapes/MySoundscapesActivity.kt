package com.example.audioproject.mySoundscapes

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.audioproject.*
import com.example.audioproject.Soundscapes.soundscapes
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_add_sound.*
import kotlinx.android.synthetic.main.fragment_my_soundscapes.*
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

    private fun playSoundscape(sounds: ArrayList<DemoApi.Model.Sound>, volume: ArrayList<Float>) {
        val sourceList = ArrayList<String>()

        for (sound in sounds) {
            sourceList.add(sound.previews.preview_hq_mp3)
        }

        for ((index, source) in sourceList.withIndex()) {
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
                        setOnCompletionListener {}
                        setDataSource(source)

                        // setVolume(left: Float, right: Float) - from volumelist[index]
                        setVolume(volume[index], volume[index])
                        prepare()
                    }
                }
                play.await()
                mp.start()
            }
        }
    }


    override fun onPlay(soundscape: Soundscape, position: Int) {
        Log.d("ss", soundscape.ssSounds.toString() + "clicked")
        Log.d("ss", soundscape.name)
        Log.d("ss", soundscapes[0].ssSounds.toString())
        playSoundscape(soundscape.ssSounds, soundscape.volume)
    }

    override fun onDel(soundscape: Soundscape, position: Int) {
        soundscapeList.removeViewAt(position)
        soundscapes.remove(soundscape)
        var prefString = Gson().toJson(soundscapes)
        val sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(com.example.audioproject.Tag.TAG, prefString)
            commit()
        }
    }
}