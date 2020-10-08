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

/**
 * View for list of created soundscapes
 * @see soundscapes
 */
class MySoundscapesFragment : Fragment() {
    private lateinit var currentContext: Context
    private lateinit var listener: OnSoundscapeSelected
    companion object {
        fun newInstance() = MySoundscapesFragment()
    }

    override fun onAttach(context: Context) {
        currentContext = context
        super.onAttach(context)
        if (context is OnSoundscapeSelected) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_soundscapes, container, false)
    }

    /**
     * gets a list of soundscapes from shared preferences and puts it to a singleton list object
     * then fills recyclerview with the list
     * @see Soundscapes.soundscapes
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPref = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE) ?: return
        val value = sharedPref.getString(TAG, "null")
        Log.d("sharedpref", value!!)
        val ss = Gson().fromJson(value, SoundlistJson::class.java)
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
}