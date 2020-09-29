package com.example.audioproject.addSoundscapes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.audioproject.MainActivity
import com.example.audioproject.R
import kotlinx.android.synthetic.main.category_list_item.view.*

class RecyclerAdapter(items: ArrayList<String>):
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val categories = items

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var categoryName: TextView = itemView.categoryListItem

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                val context = itemView.context
                Toast.makeText(itemView.context,"oof",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_list_item,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
       holder.categoryName.text = categories[position]
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}