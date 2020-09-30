package com.example.audioproject.mySoundscapes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.audioproject.R

/* TODO: mySoundScapes-activity's fragments/ functionality
        -> list of soundscapes with the functionality to play whatever of them the user wants
    */

class MySoundscapesActivity : AppCompatActivity(), OnSoundscapeSelected {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_soundscape)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mySoundscapeContainer, MySoundscapesFragment.newInstance())
                .commit()
        }
    }

    override fun onSelect(result: String, position: Int) {
        // TODO: Add functionality
    }
}