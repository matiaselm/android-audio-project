package com.example.audioproject.addSoundscapes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.audioproject.R
import kotlinx.android.synthetic.main.activity_new_soundscape.*
import kotlinx.android.synthetic.main.category_list_item.view.*
import kotlinx.android.synthetic.main.fragment_category_search.*
import java.util.*
import kotlin.collections.ArrayList

class CategorySearchFragment : Fragment() {
    private val categories = ArrayList<String>()
    private lateinit var currentContext: Context
    private lateinit var listener: OnCategorySelected
    private lateinit var viewManager: LinearLayoutManager

    companion object {
        fun newInstance() = CategorySearchFragment()
    }

    override fun onAttach(context: Context) {
        currentContext = context
        super.onAttach(context)
        if(context is OnCategorySelected){
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

    private fun searchCategories(category: String){
        val resultListFragment = ResultListFragment.newInstance(category)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.newSScontainer, resultListFragment, "stuff")
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categories.add("Banjo")
        categories.add("People")
        categories.add("Cats")
        categories.add("Nature")
        viewManager = LinearLayoutManager(activity)
        categoryList.layoutManager = viewManager
        categoryList.setHasFixedSize(true)
        var adapter = CategoryRecyclerAdapter(categories)
        categoryList.adapter = adapter

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

   internal inner class CategoryRecyclerAdapter(
        private val items: ArrayList<String>,
    ) : RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder>() {

        internal inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var categoryName: TextView = itemView.categoryListItem

            fun init(category: String, action: OnCategorySelected) {

                categoryName.text = category

            itemView.setOnClickListener {
                action.onSelect(category, adapterPosition)
                }


            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CategoryRecyclerAdapter.CategoryViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_list_item, parent, false)

            return CategoryViewHolder(v)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            val result = items[position]
            holder.init(result, listener)
        }

        override fun getItemCount(): Int{
            return items.count()
        }
    }
}

    interface OnCategorySelected {
        fun onSelect(result: String, position: Int)
    }

