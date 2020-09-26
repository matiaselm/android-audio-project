package com.example.audioproject.addSoundscapes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.audioproject.R
import kotlinx.android.synthetic.main.activity_new_sound_scape.*

class ResultListFragment : Fragment() {

    companion object {
        fun newInstance(category: String) : ResultListFragment {
            val args = Bundle()
            args.putSerializable("category", category)
           var fragment = ResultListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var stuff = arguments!!.getSerializable("category")
        Log.d("stuff", stuff.toString())
        return inflater.inflate(R.layout.fragment_result_list, newSScontainer, false)
    }
}