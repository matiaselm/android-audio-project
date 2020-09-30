package com.example.audioproject.addSoundscapes

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.audioproject.DemoApi
import com.example.audioproject.MainActivity
import com.example.audioproject.R
import com.example.audioproject.Tag
import com.example.audioproject.WebServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

// TODO: Save-btn onClick -> creates soundScape obj from selected sounds and adds them to globalModel-list
// TODO: Play-btn onClick -> play all audio on the list simultaneously

class NewSoundscapeActivity : AppCompatActivity(), OnSoundSelected {
    var soundList = ArrayList<DemoApi.Model.Result>()

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
                            Toast.makeText(
                                this@NewSoundscapeActivity,
                                "Finished playing: $soundName",
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
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_soundscape)
        Log.d("stuff", soundList.toString())
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.newSScontainer, AddSoundFragment.newInstance(soundList))
                .commit()
        }
    }

    override fun onClickPlay(result: DemoApi.Model.Result, position: Int) {

        playAudio(result.id)

    }

    override fun onClickSound(result: DemoApi.Model.Result, position: Int) {

        //TODO get the actual sound from result, save preview links
        soundList.add(result)
        Log.d("stuff", "added ${result}")
        Log.d("stuff", soundList.toString())
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.newSScontainer, AddSoundFragment.newInstance(soundList))
            .commit()

    }
}