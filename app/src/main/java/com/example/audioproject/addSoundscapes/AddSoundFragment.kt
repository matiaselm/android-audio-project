package com.example.audioproject.addSoundscapes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.audioproject.DemoApi
import com.example.audioproject.R
import com.example.audioproject.Soundlist.sounds
import kotlinx.android.synthetic.main.activity_new_soundscape.*
import kotlinx.android.synthetic.main.fragment_add_sound.*

class AddSoundFragment : Fragment() {

    lateinit var currentContext: Context

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
    }

    internal inner class MySoundsRecyclerAdapter(sounds: ArrayList<DemoApi.Model.Sound?>) :
            RecyclerView.Adapter<MySoundsRecyclerAdapter.ViewHolder>() {

        internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun initialize(sound: DemoApi.Model.Sound){

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemCount() = sounds.count()
    }
}