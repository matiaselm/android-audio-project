package com.example.audioproject.addSoundscapes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioproject.R
import kotlinx.android.synthetic.main.activity_new_soundscape.*
import kotlinx.android.synthetic.main.fragment_category_search.*

class CategorySearchFragment : Fragment() {

    private val categories = ArrayList<String>()
    private lateinit var currentContext: Context

    companion object {
        fun newInstance() = CategorySearchFragment()
    }

    override fun onAttach(context: Context) {
        currentContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categories.add("Banjo")
        categories.add("People")
        categories.add("Cats")
        categories.add("Nature")

        return inflater.inflate(R.layout.fragment_category_search, newSScontainer, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        categoryList.apply {
            layoutManager = LinearLayoutManager(currentContext)
            adapter = RecyclerAdapter(categories)
        }

/*
        category1.text = categories[0]
        category2.text = categories[1]
        category3.text = categories[2]
        category4.text = categories[3]

        category1.setOnClickListener {
            val resultListFragment = ResultListFragment.newInstance(category1.text.toString())
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.newSScontainer, resultListFragment, "stuff")
                ?.addToBackStack(null)
                ?.commit()
        }

        category2.setOnClickListener {
            val resultListFragment = ResultListFragment.newInstance(category2.text.toString())
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.newSScontainer, resultListFragment, "stuff")
                ?.addToBackStack(null)
                ?.commit()
        }

        category3.setOnClickListener {
            val resultListFragment = ResultListFragment.newInstance(category3.text.toString())
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.newSScontainer, resultListFragment, "stuff")
                ?.addToBackStack(null)
                ?.commit()
        }

        category4.setOnClickListener {
            val resultListFragment = ResultListFragment.newInstance(category4.text.toString())
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.newSScontainer, resultListFragment, "stuff")
                ?.addToBackStack(null)
                ?.commit()
        }

        */


    }


}