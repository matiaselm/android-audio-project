package com.example.audioproject.mySoundscapes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.audioproject.R
import kotlinx.android.synthetic.main.category_list_item.view.*
import kotlinx.android.synthetic.main.category_list_item.view.categoryListItem
import kotlinx.android.synthetic.main.soundscape_list_item.view.*

class MySoundscapeRecyclerAdapter(
    items: ArrayList<String>,
    clickListener: OnSoundscapeSelected
) :
    RecyclerView.Adapter<MySoundscapeRecyclerAdapter.ViewHolder>() {

    private val soundscapes = items
    private val listener = clickListener

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private var soundscapeName: TextView = itemView.soundscapeName

        fun init(action: OnSoundscapeSelected, result: String) {
            soundscapeName.text = result

            itemView.setOnClickListener {
                val position: Int = adapterPosition
                val name = itemView.soundscapeName.text.toString()

                Toast.makeText(itemView.context, name, Toast.LENGTH_SHORT).show()

                action.onSelect(name, position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MySoundscapeRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.soundscape_list_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MySoundscapeRecyclerAdapter.ViewHolder, position: Int) {
        val result = soundscapes[position]
        holder.init(listener, result)
    }

    override fun getItemCount(): Int {
        return soundscapes.size
    }
}

interface OnSoundscapeSelected {
    fun onSelect(result: String, position: Int)
}