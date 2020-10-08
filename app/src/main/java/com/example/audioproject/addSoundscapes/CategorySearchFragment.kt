package com.example.audioproject.addSoundscapes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audioproject.CategoryList.categories
import com.example.audioproject.R
import kotlinx.android.synthetic.main.activity_new_soundscape.*
import kotlinx.android.synthetic.main.fragment_category_search.*

/**
 * Fragment to list categories for searching sounds
 */
class CategorySearchFragment : Fragment() {
    private lateinit var currentContext: Context
    lateinit var listener: OnCategorySelected

    companion object {
        fun newInstance() = CategorySearchFragment()
    }

    override fun onAttach(context: Context) {
        currentContext = context
        super.onAttach(context)
        if (context is OnCategorySelected) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_category_search, newSScontainer, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        categoryList.apply {
            layoutManager = LinearLayoutManager(currentContext)
            adapter = CategoryRecyclerAdapter(categories, listener)
        }
    }
}