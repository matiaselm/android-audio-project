package com.example.audioproject.mySoundscapes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioproject.CategoryList
import com.example.audioproject.R
import com.example.audioproject.Soundlist
import com.example.audioproject.Soundscapes.soundscapes
import kotlinx.android.synthetic.main.activity_new_soundscape.*
import kotlinx.android.synthetic.main.fragment_category_search.*

class MySoundscapesFragment : Fragment() {
    private lateinit var currentContext: Context
    lateinit var listener: OnSoundscapeSelected

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
        return inflater.inflate(R.layout.fragment_category_search, container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        categoryList.apply {
            layoutManager = LinearLayoutManager(currentContext)
            adapter = MySoundscapeRecyclerAdapter(soundscapes, listener)
        }

        // Log.d("status", soundscapes[0].ssSounds.toString())

    }
}