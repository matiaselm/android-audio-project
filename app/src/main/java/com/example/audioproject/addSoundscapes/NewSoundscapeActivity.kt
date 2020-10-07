package com.example.audioproject.addSoundscapes

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.audioproject.*
import com.example.audioproject.Soundlist.sounds
import kotlinx.coroutines.*
import java.net.URL

class NewSoundscapeActivity : AppCompatActivity(), OnSoundSelected, OnCategorySelected,
    OnClipSelected {
    private val viewmodel = SoundViewModel()

    // Called on the recyclerView of fetched audio
    private fun playAudioFromResult(id: Int, button: Button) {
        var result: DemoApi.Model.Sound? = null
        button.isEnabled = false

        Log.d(Tag.TAG, "playAudio id: $id")
        lifecycleScope.launch(Dispatchers.IO) {
            result = WebServiceRepository().getSound(id.toString())

            Log.d(Tag.TAG, "lifecycleScope, result: $result")

            if (result != null) {
                val soundUrl = URL(result!!.previews.preview_hq_mp3) // High quality mp3
                val soundName = formatResult(result!!.name)

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
                            button.isEnabled = true
                            Toast.makeText(
                                this@NewSoundscapeActivity,
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
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_soundscape)
        Log.d("stuff", sounds.toString())
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.newSScontainer, AddSoundFragment.newInstance(sounds))
                .commit()
        }
    }

    override fun onClickPlay(result: DemoApi.Model.Result, position: Int, button: Button) {
        playAudioFromResult(result.id, button)
    }

    override fun onClickSound(result: DemoApi.Model.Result, position: Int) {
        var sound: DemoApi.Model.Sound? = null
        lifecycleScope.launch {
            // async(Dispatchers.IO) {
            runBlocking {
                sound = WebServiceRepository().getSound(result.id.toString())
                sounds.add(sound!!)
                viewmodel.liveSounds.value = sounds
                Log.d("stuff", sound.toString())
            }/*.await()*/

        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.newSScontainer, AddSoundFragment.newInstance(sounds))
            .commit()

    }

    override fun onSelect(result: String, position: Int) {
        val resultListFragment = ResultListFragment.newInstance(result)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.newSScontainer, resultListFragment, "stuff")
            .addToBackStack(null)
            .commit()
    }


    override fun onPlaySound(sound: DemoApi.Model.Sound, position: Int, button: Button) {
        button.isEnabled = false
        lifecycleScope.launch(Dispatchers.IO) {
            val soundUrl = URL(sound.previews.preview_hq_mp3) // High quality mp3
            val soundName = formatResult(sound.name)

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
                        button.isEnabled = true
                        Toast.makeText(
                            this@NewSoundscapeActivity,
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
        }
    }

    override fun onDeleteSound(sound: DemoApi.Model.Sound, position: Int) {
        Log.d("onDelete", "onDelete called")
        Log.d("onDelete", viewmodel.liveSounds.value.toString())
        sounds.remove(sound)
        viewmodel.liveSounds.value = sounds
        Log.d("onDelete", viewmodel.liveSounds.value.toString())
    }
}