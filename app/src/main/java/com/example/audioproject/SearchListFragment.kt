package com.example.audioproject

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_search_list.*
import kotlinx.android.synthetic.main.recycler_item_search.view.*
import kotlinx.coroutines.*
import retrofit2.http.Url
import java.net.URL

class SearchListFragment : Fragment() {
    lateinit var viewModel: MainViewModel
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var listener: OnResultSelected


    companion object {
        fun newInstance() = SearchListFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnResultSelected) {
            listener = context
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // get viewmodel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // use viewmodels queryWithText to start a HTTP GET request
        // Observe the result as livedata (access data with -it-)
        viewModel.results.observe(this, {
            if(it!=null){
                recycler.adapter = SearchRecyclerAdapter(it.results, listener)
            } else {
                val results = ArrayList<DemoApi.Model.Result>()
                val tags = ArrayList<String>()
                tags.add("couldn't connect")
                results.add(DemoApi.Model.Result(0,"none","couldn't connect to service",tags,"Server down"))
                recycler.adapter = SearchRecyclerAdapter(results, listener)
            }

        })

        setAdapter()

        searchButton.setOnClickListener() {
            viewModel.queryWithText(searchField.text.toString())
        }
        //.observe(this, {userlist.adapter = UserRecyclerAdapter(it?.sortedBy { that -> that.lastname }, this)})

        //TODO make a textfield and a button to search for different data
        //TODO display the livedata in a recyclerview below
    }

    fun setAdapter() {
        viewManager = LinearLayoutManager(activity)

        recycler.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_list, container, false)
    }

    internal inner class SearchRecyclerAdapter(
        private val results: List<DemoApi.Model.Result>?,
        private var clickListener: OnResultSelected
    ) : RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder>() {

        internal inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val name: TextView = view.result_name
            private val username: TextView = view.result_username
            private val addButton: Button = view.addButton
            private val playButton: Button = view.playButton

            @ExperimentalCoroutinesApi
            fun initialize(result: DemoApi.Model.Result, action: OnResultSelected) {
                name.text = result.name
                username.text = result.username

                addButton.setOnClickListener() {
                    action.onClickResult(result, adapterPosition)
                }
                playButton.setOnClickListener() {
                    action.onClickPlay(result, adapterPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_search, parent, false)
            return SearchViewHolder(view)
        }

        @ExperimentalCoroutinesApi
        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            val result = results!![position]
            holder.initialize(result, listener)
        }

        override fun getItemCount() = results!!.count()
    }
}

//TODO implement the onclick on each item
interface OnResultSelected {
    fun onClickResult(result: DemoApi.Model.Result, position: Int)
    fun onClickPlay(result: DemoApi.Model.Result, position: Int)
}
