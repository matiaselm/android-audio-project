package com.example.audioproject.mySoundscapes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.audioproject.R
import com.example.audioproject.Soundscape
import kotlinx.android.synthetic.main.categoryresult_list_item.view.*

class MySoundscapeRecyclerAdapter(
    items: ArrayList<Soundscape>,
    clickListener: OnSoundscapeSelected
) :
    RecyclerView.Adapter<MySoundscapeRecyclerAdapter.ViewHolder>() {

    private val soundscapes = items
    private val listener = clickListener

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private var soundscapeName: TextView = itemView.name
        private var soundscapeInfo: TextView = itemView.info
        private var playButton: Button = itemView.playButton
        private var modifyButton: Button = itemView.addButton
        private val infoText = soundscapeInfo.context.getString(R.string.soundscape_size)
        @SuppressLint("SetTextI18n")
        fun init(action: OnSoundscapeSelected, soundscape: Soundscape) {
            soundscapeName.text = soundscape.name
            soundscapeInfo.text = infoText + soundscape.ssSounds.size.toString()
            modifyButton.text = itemView.context.getString(R.string.modify)

            playButton.setOnClickListener {
                action.onPlay(soundscape, adapterPosition)
            }

            modifyButton.setOnClickListener{
                val position: Int = adapterPosition
                val name = itemView.name.text.toString()

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
            .inflate(R.layout.categoryresult_list_item, parent, false)

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
    fun onPlay(soundscape: Soundscape, position: Int)
}