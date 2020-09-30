package com.example.audioproject.addSoundscapes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.audioproject.DemoApi
import com.example.audioproject.MainActivity
import com.example.audioproject.R

class NewSoundscapeActivity : AppCompatActivity(), OnSoundSelected {
    var soundList = ArrayList<DemoApi.Model.Result>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_soundscape)
        Log.d("stuff", soundList.toString())
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.newSScontainer, AddSoundFragment.newInstance(soundList))
                .commit()
        }
    }

    override fun onClickPlay(result: DemoApi.Model.Result, position: Int) {

    }

    override fun onClickSound(result: DemoApi.Model.Result, position: Int) {

        //TODO get the actual sound from result, save preview links
        soundList.add(result)
        Log.d("stuff", "added ${result}")
        Log.d("stuff", soundList.toString())
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.newSScontainer, AddSoundFragment.newInstance(soundList))
            .commit()

    }
}