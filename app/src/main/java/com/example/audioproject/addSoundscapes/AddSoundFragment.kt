package com.example.audioproject.addSoundscapes

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.audioproject.*
import com.example.audioproject.Soundlist.sounds
import com.example.audioproject.Soundscapes.soundscapes
import com.example.audioproject.Tag.TAG
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.activity_new_soundscape.*
import kotlinx.android.synthetic.main.fragment_add_sound.*
import kotlinx.android.synthetic.main.sound_list_item.view.*
import kotlinx.coroutines.*
import java.net.URL
import com.google.gson.Gson

/**
 * This fragment opens up a view for creating a new soundscape object
 */

class AddSoundFragment : Fragment() {

    private lateinit var currentContext: Context
    lateinit var listener: OnClipSelected
    private var viewModel = SoundViewModel()
    lateinit var volumeList: ArrayList<Float>

    /**
     * Plays all the sounds you have added for creating the soundscape
     * @param sounds ArrayList of sounds
     * @param volume ArrayList of volumes for the sounds
     */
    private fun playSoundscape(sounds: ArrayList<DemoApi.Model.Sound>, volume: ArrayList<Float>) {
        val sourceList = ArrayList<String>()

        if (sourceList.isNotEmpty()) {
            playSoundscapeButton.isEnabled = false

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
                            setOnCompletionListener {
                                playSoundscapeButton.isEnabled = true
                            }
                            setDataSource(source)
                            setVolume(volume[index], volume[index])
                            prepare()
                        }
                    }
                    play.await()
                    mp.start()
                }
            }
        }
    }

    //saves soundscape to globalmodel list of soundscapes and to shared preferences

    /**
     * Saves a soundscape to a globalmodel list and then that globalmodel list to shared preferences
     * clears all sounds that have been selected for the soundscapes for the creation of new one
     * @see soundscapes
     */
    private fun saveSoundscape() {

        if (!soundscapeNameInput.text.isNullOrEmpty() && sounds.isNotEmpty()) {
            val arraySounds: ArrayList<DemoApi.Model.Sound> = sounds
            val soundscape = Soundscape(soundscapeNameInput.text.toString(), arraySounds, volumeList)
            soundscapes.add(soundscape)
            val prefString = Gson().toJson(soundscapes)
            val sharedPref = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString(TAG, prefString)
                commit()
            }

            sounds.clear()
            volumeList.clear()
            soundscapeNameInput.text!!.clear()
            addSoundTextView.visibility = View.VISIBLE
            soundList.removeAllViews()

            Toast.makeText(
                currentContext,

                "${currentContext.getString(R.string.saved_soundscape_msg)} ${soundscape.name}",

                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                currentContext,

                currentContext.getString(R.string.error_saving_soundscape), Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    companion object {
        /**
         * creates new instance of the fragment
         * @see AddSoundFragment
         */
        fun newInstance(soundList: ArrayList<DemoApi.Model.Sound>): AddSoundFragment {
            val args = Bundle()
            args.putSerializable("key", soundList)
            val fragment = AddSoundFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_sound, newSScontainer, false)
    }

    override fun onAttach(context: Context) {
        currentContext = context
        super.onAttach(context)
        if (context is OnClipSelected) {
            listener = context
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (sounds.size > 0) {
            addSoundTextView.visibility = View.GONE
        }

        saveSoundscapeButton.setOnClickListener {
            saveSoundscape()
        }

        playSoundscapeButton.setOnClickListener {
            playSoundscape(sounds, volumeList)
        }

        /**
         * fab sends the user into another fragment to add new sounds to the soundscape
         * @see CategorySearchFragment
         */
        fab.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.newSScontainer, CategorySearchFragment.newInstance())
                ?.addToBackStack(null)
                ?.commit()
        }
        viewModel.liveSounds.value = sounds
        soundList.apply {
            layoutManager = LinearLayoutManager(activity)
        }
        /**
         * makes an observer that when called gets a list of sounds and puts it on recyclerview
         */
        val nameObserver = Observer<MutableList<DemoApi.Model.Sound>> { sounds ->
            soundList.adapter = MySoundsRecyclerAdapter(sounds)
        }
        /**
         * observes livedata
         */
        viewModel.liveSounds.observe(viewLifecycleOwner, nameObserver)
        volumeList = ArrayList<Float>()

        //overrides back button function to go back to navigatorfragment instead of breaking the adding cycle
        /**
         * overrides back button
         */
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    /**
     * Recyclerviews adapter
     * @param mySounds list of sounds
     */
    internal inner class MySoundsRecyclerAdapter(var mySounds: MutableList<DemoApi.Model.Sound>) :
        RecyclerView.Adapter<MySoundsRecyclerAdapter.ViewHolder>() {

        internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val soundImage: ImageView = view.soundImage
            private val soundName: TextView = view.singleSoundName
            private val playButton: Button = view.soundPlayButton
            private val removeButton: Button = view.soundRemoveButton
            private val slider: Slider = view.volumeSlider

            /**
             * initializes all things inside single item view in recyclerview
             * @param sound a single sound object
             * @param action onClick action
             */
            fun initialize(sound: DemoApi.Model.Sound, action: OnClipSelected) {

                volumeList.add(0.8F)
                /**
                 * for each sound object show the objects image in an imageview
                 */
                val uri = URL(sound.images.waveform_m)
                lifecycleScope.launch {
                    showImg(getImage(uri), soundImage)
                }

                Log.d(TAG, soundImage.toString())
                soundName.text = formatResult(sound.name)

                playButton.setOnClickListener {
                    action.onPlaySound(sound, adapterPosition, playButton)
                }

                removeButton.setOnClickListener {
                    soundList.removeViewAt(adapterPosition)
                    volumeList.removeAt(adapterPosition)
                    sounds.removeAt(adapterPosition)
                }
                /**
                 * slider for changing the volume of a single sound item
                 */
                slider.addOnChangeListener { _, value, _ ->
                    Log.d(TAG, "Volume changed to: $value")
                    volumeList[adapterPosition] = value
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.sound_list_item, parent, false)
            return ViewHolder(view)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val result = mySounds[position]
            holder.initialize(result, listener)
        }
        override fun getItemCount() = mySounds.count()

        /**
         * makes a bitmap image out of images URL
         * @param url
         */
        private suspend fun getImage(url: URL) = withContext(Dispatchers.IO) {
            val allText = url.openStream()
            val decodeStream = BitmapFactory.decodeStream(allText)

            return@withContext decodeStream
        }
        //shows image in a given imageview
        /**
         * shows an image in a given imageview
         * @param i bitmap
         * @param image imageview thats going to show the image
         */
        private fun showImg(i: Bitmap, image: ImageView) {
            image.setImageBitmap(i)
        }
    }
}

/**
 * class for holding livedata of sounds
 */
class SoundViewModel : ViewModel() {
    val liveSounds: MutableLiveData<MutableList<DemoApi.Model.Sound>> by lazy {
        MutableLiveData<MutableList<DemoApi.Model.Sound>>()
    }
}

/**
 * onClick actions for recyclerview items
 */
interface OnClipSelected {
    fun onPlaySound(sound: DemoApi.Model.Sound, position: Int, button: Button)
}