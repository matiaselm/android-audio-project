package com.example.audioproject.addSoundscapes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.audioproject.R
import kotlinx.android.synthetic.main.activity_new_sound_scape.*
import kotlinx.android.synthetic.main.fragment_category_search.*

class CategorySearchFragment: Fragment(){

    companion object{
        fun newInstance() = CategorySearchFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_search, newSScontainer,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        category1.text = "category 1"
        category2.text = "category 2"
        category3.text = "category 3"
        category4.text = "category 4"
        category1.setOnClickListener{
            val resultListFragment = ResultListFragment.newInstance(category1.text.toString())
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.container, resultListFragment, "stuff")
                ?.addToBackStack(null)
                ?.commit()
        }

    }


}