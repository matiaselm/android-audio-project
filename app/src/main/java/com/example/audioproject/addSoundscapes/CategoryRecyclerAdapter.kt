package com.example.audioproject.addSoundscapes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.audioproject.R
import kotlinx.android.synthetic.main.category_list_item.view.*

/**
 * Recyclerview adapter for showing different categories
 */
class CategoryRecyclerAdapter(
    items: ArrayList<String>,
    clickListener: OnCategorySelected
) :
    RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>() {

    private val categories = items
    private val listener = clickListener

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

       private var categoryName: TextView = itemView.categoryListItem

        /**
         * initializes all listitems of recyclerview
         * @param result string value of a category name
         * @param action onClick action
         */
        fun init(action: OnCategorySelected, result: String) {
            categoryName.text = result

            itemView.setOnClickListener {
                val position: Int = adapterPosition
                val categoryName = itemView.categoryListItem.text.toString()

                Toast.makeText(itemView.context, categoryName, Toast.LENGTH_SHORT).show()

                action.onSelect(categoryName, position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_list_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryRecyclerAdapter.ViewHolder, position: Int) {
        val result = categories[position]
        holder.init(listener, result)
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}


interface OnCategorySelected {
    fun onSelect(result: String, position: Int)
}
