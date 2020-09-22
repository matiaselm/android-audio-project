package com.example.audioproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //get viewmodel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //use viewmodels queryWithText to start a HTTP GET request
        viewModel.queryWithText("piano")

        //Observe the result as livedata (access data with -it-)
        viewModel.results.observe(this, {Log.d("stuff", it.toString())})
        //.observe(this, {userlist.adapter = UserRecyclerAdapter(it?.sortedBy { that -> that.lastname }, this)})

        //TODO make a textfield and a button to search for different data
        //TODO display the livedata in a recyclerview below
        //TODO Polish your bolete or something
    }
}