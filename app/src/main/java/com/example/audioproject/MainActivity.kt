package com.example.audioproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), OnResultSelected {
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, SearchListFragment.newInstance())
                .commit()
        }

        //TODO Polish your bolete or something
        //TODO alot of stuff really.
    }

    override fun onClickResult(result: DemoApi.Model.Result, position: Int) {
        Log.d("onClick", result.id.toString() + "add")
    }

    override fun onClickPlay(result: DemoApi.Model.Result, position: Int) {
        Log.d("onClick", result.id.toString() + "play")
    }
}