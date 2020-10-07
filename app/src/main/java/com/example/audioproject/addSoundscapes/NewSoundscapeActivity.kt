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

/**
 * NewSoundscapeActivity holds all the functionality of the fragments in adding new soundscapes
 */
class NewSoundscapeActivity : AppCompatActivity(), OnSoundSelected, OnCategorySelected,
    OnClipSelected {
    private val viewmodel = SoundViewModel()

    // Called on the recyclerView of fetched audio
    /**
     * plays a single sound from soundlist
     * @param id id of a sound to search from freesound
     * @param button to disable it while playing sounds so you cant play multiple sounds at the same time
     */
    private fun playAudioFromResult(id: Int, button: Button) {
        var result: DemoApi.Model.Sound?
        button.isEnabled = false

        Log.d(Tag.TAG, "playAudio id: $id")
        /**
         * fetches a single sound with id from freesound
         * @see WebServiceRepository
         */
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

    /**
     * oncreate sets the view as AddSoundFragment
     */
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

    /**
     * onClick method
     * plays a single sound from a list of search results
     * @param result a single item from a list of search results
     * @param position position of the item on the list
     * @param button button of recycler item position
     * @see playAudioFromResult
     */
    override fun onClickPlay(result: DemoApi.Model.Result, position: Int, button: Button) {
        playAudioFromResult(result.id, button)
    }

    /**
     * onClick method
     * gets the information of the sound from freesound with the id of the result
     * adds the selected sound to your list of creating a soundscape
     * @param result single search result
     * @param position position on the list
     */
    override fun onClickSound(result: DemoApi.Model.Result, position: Int) {
        var sound: DemoApi.Model.Sound? = null
        lifecycleScope.launch {
            runBlocking {
                /**
                 * freesound api returns first a list of vague information including id:s of the sounds
                 * here we search for the actual information of the single sound item with id
                 */
                sound = WebServiceRepository().getSound(result.id.toString())
                sounds.add(sound!!)
                viewmodel.liveSounds.value = sounds
                Log.d("stuff", sound.toString())
            }

        }
        /**
         * returns back to AddSoundFragment
         */
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.newSScontainer, AddSoundFragment.newInstance(sounds))
            .commit()

    }

    /**
     * onClick method
     * on selecting a category this starts a new fragment, resultListfragment
     * @param result is for holding extra information for the fragment
     */
    override fun onSelect(result: String, position: Int) {
        val resultListFragment = ResultListFragment.newInstance(result)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.newSScontainer, resultListFragment, "stuff")
            .addToBackStack(null)
            .commit()
    }

    /**
     * onClick method
     * plays a single sound item
     * @param sound
     * @param position
     * @param button
     */
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
                    /**
                     * onCompletionlistener called when sound has finished playing
                     */
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
}