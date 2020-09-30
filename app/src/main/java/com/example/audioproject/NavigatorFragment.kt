package com.example.audioproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.audioproject.addSoundscapes.NewSoundscapeActivity
import com.example.audioproject.mySoundscapes.MySoundscapeActivity
import kotlinx.android.synthetic.main.fragment_navigator.*

class NavigatorFragment: Fragment() {

    companion object {
        fun newInstance() = NavigatorFragment()
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button1.setOnClickListener {
            val intent = Intent(activity, NewSoundscapeActivity::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.container, SearchListFragment.newInstance())
                ?.addToBackStack(null)
                ?.commit()
        }
        button3.setOnClickListener {
            val intent = Intent(activity, MySoundscapeActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_navigator, container, false)
    }

}

interface OnSelectFragment