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
import kotlinx.android.synthetic.main.activity_new_soundscape.*
import kotlinx.android.synthetic.main.fragment_add_sound.*
import kotlinx.android.synthetic.main.sound_list_item.view.*
import kotlinx.coroutines.*
import java.net.URL
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import okhttp3.internal.notify
import okhttp3.internal.notifyAll


class AddSoundFragment : Fragment() {

    lateinit var currentContext: Context
    lateinit var listener: OnClipSelected
    private var viewModel = SoundViewModel()

    private fun playAudio(sounds: ArrayList<DemoApi.Model.Sound>) {

        val sourceList = ArrayList<String>()
        for (sound in sounds) {
            sourceList.add(sound.previews.preview_hq_mp3)
        }

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
                        setOnCompletionListener {}
                        setDataSource(source)
                        prepare()
                    }
                }
                play.await()
                mp.start()
            }
        }
    }

    private fun saveSoundscape(){
        val arraySounds: ArrayList<DemoApi.Model.Sound> = sounds
        val soundscape = Soundscape(soundscapeNameInput.text.toString(), arraySounds)
        Log.d("add", soundscape.ssSounds.toString())
        soundscapes.add(soundscape)
        //sounds.clear poistaa kaikki yllä tehdyn soundscape objektin äänet vaikka sounds ei ole sen objektin kanssa missään tekemisissä

        sounds.clear()

        Toast.makeText(currentContext, "Saved soundscape ${soundscape.name}", Toast.LENGTH_SHORT).show()

        // Aloittaa uuden aktiviteetin ladatakseen äänilistan uudestaan, tähän parempi tapa myöhemmin
        startActivity(Intent(activity, NewSoundscapeActivity::class.java))
    }

    companion object {
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
            playAudio(sounds)
        }

        fab.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.newSScontainer, CategorySearchFragment.newInstance())
                ?.addToBackStack(null)
                ?.commit()
        }
        viewModel.liveSounds.value = sounds
        Log.d("wee", viewModel.liveSounds.value.toString())
        soundList.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        val nameObserver = Observer<MutableList<DemoApi.Model.Sound>> { sounds ->
            Log.d("new", "brand spanking new data $sounds")
            soundList.adapter = MySoundsRecyclerAdapter(sounds)
        }
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.liveSounds.observe(viewLifecycleOwner, nameObserver)

       // vmp.liveRecords.observe(this, {soundList.adapter = MySoundsRecyclerAdapter(it)})
        //overrides back button function to go back to navigatorfragment instead of breaking the adding cycle
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    internal inner class MySoundsRecyclerAdapter(var mySounds: MutableList<DemoApi.Model.Sound>) :
        RecyclerView.Adapter<MySoundsRecyclerAdapter.ViewHolder>() {

        internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val soundImage: ImageView = view.soundImage
            private val soundName: TextView = view.singleSoundName
            private val soundUserName: TextView = view.soundUserName
            private val playButton: Button = view.soundPlayButton
            private val deleteButton: Button = view.soundRemoveButton
            fun initialize(sound: DemoApi.Model.Sound, action: OnClipSelected) {

                val uri = URL(sound.images.waveform_m)
                lifecycleScope.launch {
                    showImg(getImage(uri), soundImage)
                }
                Log.d(TAG, soundImage.toString())
                soundName.text = sound.name
                soundUserName.text = sound.username

                itemView.setOnClickListener {
                    action.onSelectSound(sound, adapterPosition)
                }

                deleteButton.setOnClickListener{
                    action.onDeleteSound(sound, adapterPosition)
                }

                playButton.setOnClickListener{
                    action.onPlaySound(sound, adapterPosition)
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

        private suspend fun getImage(url: URL) = withContext(Dispatchers.IO) {
            val allText = url.openStream()
            val decodeStream = BitmapFactory.decodeStream(allText)

            return@withContext decodeStream
        }

        private fun showImg(i: Bitmap, image: ImageView) {
            image.setImageBitmap(i)
        }

        public fun update(){
            notifyDataSetChanged()
        }
    }
}

class SoundViewModel: ViewModel() {

    val liveSounds: MutableLiveData<MutableList<DemoApi.Model.Sound>> by lazy {
        MutableLiveData<MutableList<DemoApi.Model.Sound>>()
    }

        val liveRecords = liveData(Dispatchers.IO) {
            emit(sounds)
        }


    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
    fun addSound(sound: DemoApi.Model.Sound) {
        liveSounds.value?.add(sound)
        liveSounds.notifyObserver()
    }
    fun delSound(sound: DemoApi.Model.Sound){
        liveSounds.value?.remove(sound)
        liveSounds.notifyObserver()
    }
    }



interface OnClipSelected {
    fun onSelectSound(sound: DemoApi.Model.Sound, position: Int)
    fun onPlaySound(sound: DemoApi.Model.Sound, position: Int)
    fun onDeleteSound(sound: DemoApi.Model.Sound, position: Int)
}