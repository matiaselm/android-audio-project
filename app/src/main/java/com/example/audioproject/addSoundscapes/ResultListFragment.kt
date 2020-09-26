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
import java.io.Serializable

class ResultListFragment : Fragment() {
    lateinit var category: Serializable

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("cat", category.toString())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        category = arguments!!.getSerializable("category")!!
        return inflater.inflate(R.layout.fragment_result_list, newSScontainer, false)
    }
}