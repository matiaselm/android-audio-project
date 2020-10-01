package com.example.audioproject.addSoundscapes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.audioproject.DemoApi
import com.example.audioproject.R
import com.example.audioproject.Soundlist
import com.example.audioproject.Soundlist.sounds
import com.example.audioproject.Tag.TAG
import kotlinx.android.synthetic.main.activity_new_soundscape.*
import kotlinx.android.synthetic.main.fragment_add_sound.*
import kotlinx.android.synthetic.main.sound_list_item.*
import kotlinx.android.synthetic.main.sound_list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.URL

class AddSoundFragment : Fragment() {

    lateinit var currentContext: Context
    lateinit var listener: OnClipSelected

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
        if(context is OnClipSelected){
            listener = context
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (soundList.size > 0) {
            addSoundTextView.visibility = View.GONE
        }

        fab.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.newSScontainer, CategorySearchFragment.newInstance())
                ?.addToBackStack(null)
                ?.commit()
        }
        soundList.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = MySoundsRecyclerAdapter(sounds)
        }
    }

    internal inner class MySoundsRecyclerAdapter(var mySounds: ArrayList<DemoApi.Model.Sound>) :
            RecyclerView.Adapter<MySoundsRecyclerAdapter.ViewHolder>() {



        internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            var soundImage: ImageView = view.soundImage
            var soundName: TextView = view.singleSoundName
            var soundUserName: TextView = view.soundUserName
            var playbutton: Button = view.soundPlayButton
            fun initialize(sound: DemoApi.Model.Sound, action: OnClipSelected){

                var uri = URL(sound.images.waveform_m)
                lifecycleScope.launch{
                    showImg(getImage(uri), soundImage)
                }
                Log.d(TAG, soundImage.toString())
                soundName.text = sound.name
                soundUserName.text = sound.username
                itemView.setOnClickListener{
                    action.onSelectSound(sound, adapterPosition)
                }

                playbutton.setOnClickListener{
                    //TODO onclickplay
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

       private suspend fun getImage(url: URL) = withContext(Dispatchers.IO){
            val allText = url.openStream()
           val decodeStream = BitmapFactory.decodeStream(allText)

           return@withContext decodeStream
        }

        private fun showImg(i: Bitmap, image: ImageView){
            image.setImageBitmap(i)
        }
    }
}
interface OnClipSelected {
    fun onSelectSound(sound: DemoApi.Model.Sound, position: Int)
}