package com.example.audioproject.addSoundscapes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.audioproject.DemoApi
import com.example.audioproject.MainViewModel
import com.example.audioproject.R
import kotlinx.android.synthetic.main.activity_new_soundscape.*
import kotlinx.android.synthetic.main.fragment_result_list.*
import kotlinx.android.synthetic.main.recycler_item_search.view.*
import java.io.Serializable

class ResultListFragment : Fragment() {
    private lateinit var category: Serializable
    lateinit var listener: OnSoundSelected
    lateinit var viewModel: MainViewModel
    lateinit var viewManager: LinearLayoutManager

    companion object {
        fun newInstance(category: String): ResultListFragment {
            val args = Bundle()
            args.putSerializable("category", category)
            val fragment = ResultListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSoundSelected) {
            listener = context
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setAdapter()
        viewModel.queryWithText(category.toString())
        Log.d("cat", category.toString())

        viewModel.results.observe(this, {
            if (it != null) {
                result_list.adapter = ResultRecyclerAdapter(it.results)
            } else {
                val results = ArrayList<DemoApi.Model.Result>()
                val tags = ArrayList<String>()
                tags.add("couldn't connect")
                results.add(
                    DemoApi.Model.Result(
                        0,
                        "none",
                        "couldn't connect to service",
                        tags,
                        "Server down"
                    )
                )
                result_list.adapter = ResultRecyclerAdapter(results)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        category = arguments!!.getSerializable("category")!!
        return inflater.inflate(R.layout.fragment_result_list, newSScontainer, false)
    }

    private fun setAdapter() {
        viewManager = LinearLayoutManager(activity)

        result_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }
    }

    internal inner class ResultRecyclerAdapter(private var results: List<DemoApi.Model.Result>?) : RecyclerView.Adapter<ResultRecyclerAdapter.ResultViewHolder>() {

        internal inner class ResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val playButton: Button = view.playButton
            private val name: TextView = view.result_name
            private val username: TextView = view.result_username

            fun initialize(result: DemoApi.Model.Result, action: OnSoundSelected) {
                name.text = result.name
                username.text = result.username

                playButton.setOnClickListener {
                    action.onClickPlay(result, adapterPosition)
                }

                itemView.setOnClickListener {
                    action.onClickSound(result, adapterPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_search, parent, false)
            return ResultViewHolder(view)
        }

        override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
            val result = results!![position]
            holder.initialize(result, listener)
        }

        override fun getItemCount() = results!!.count()
    }

}

interface OnSoundSelected {
    fun onClickPlay(result: DemoApi.Model.Result, position: Int)
    fun onClickSound(result: DemoApi.Model.Result, position: Int)
}