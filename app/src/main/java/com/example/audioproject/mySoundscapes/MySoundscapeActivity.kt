package com.example.audioproject.mySoundscapes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.audioproject.R

/* TODO: mySoundScapes-activity's fragments/ functionality
        -> list of soundscapes with the functionality to play whatever of them the user wants
    */

class MySoundscapeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_soundscape)
    }
}