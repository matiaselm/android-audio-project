package com.example.audioproject.mySoundscapes

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioproject.*
import com.example.audioproject.Soundscapes.soundscapes
import com.example.audioproject.Tag.TAG
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_my_soundscapes.*

class MySoundscapesFragment : Fragment() {
    private lateinit var currentContext: Context
    private lateinit var sharedPrefs: SharedPreferences

    lateinit var listener: OnSoundscapeSelected

    val gson = Gson()

    companion object {
        fun newInstance() = MySoundscapesFragment()
    }

    override fun onAttach(context: Context) {
        currentContext = context
        super.onAttach(context)
        if (context is OnSoundscapeSelected) {
            listener = context
        }

        // init sharedpreferences-instance
        val MODE = Context.MODE_PRIVATE
        sharedPrefs = context.getSharedPreferences(TAG, MODE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_soundscapes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPref = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE) ?: return
        val value = sharedPref.getString(Tag.TAG, "null")
        Log.d("sharedpref", value!!)
        var ss = Gson().fromJson<SoundlistJson>(value, SoundlistJson::class.java)
        soundscapes = ss
        Log.d("sharedpref", ss.toString())

        soundscapeList.apply {
            layoutManager = LinearLayoutManager(currentContext)
            adapter = MySoundscapeRecyclerAdapter(soundscapes, listener)
        }

        if (soundscapes.size > 0) {
            mySoundscapeTextView.visibility = View.GONE
        }
    }


    override fun onPause() {
        super.onPause()

        if (soundscapes.size > 0) {
            saveSoundscapesToPrefs()
        }
    }

    private fun saveSoundscapesToPrefs() {
        // TODO: save the whole soundscapes-list into sharedpreferences

        // How to add soundscapes into sharedpreferences without losing information?
        val soundscapesAsJson = gson.toJson(soundscapes)

        Log.d(TAG, "json: $soundscapesAsJson")

        val soundscapesDeserialized = gson.fromJson(soundscapesAsJson,Array<Soundscape>::class.java).toList()

        Log.d(TAG, soundscapesDeserialized.toString())
    }
}