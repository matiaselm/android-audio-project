package com.example.audioproject.addSoundscapes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.audioproject.DemoApi
import com.example.audioproject.R

class NewSoundscapeActivity : AppCompatActivity() {
    var soundList: ArrayList<DemoApi.Model.Sound>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_sound_scape)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.newSScontainer, AddSoundFragment.newInstance())
                .commit()
        }
    }
}