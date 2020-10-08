package com.example.audioproject

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.audioproject.Tag.TAG
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * gets saved data from shared preferences when the application starts
         */
        val sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE) ?: return
        val value = sharedPref.getString(Tag.TAG, "null")
        Log.d("sharedpref", value!!)
        var ss = Gson().fromJson<SoundlistJson>(value, SoundlistJson::class.java)
        if(ss != null) {
            Soundscapes.soundscapes = ss
        }
        /**
         * fills container with navigatorFragment
         * @see NavigatorFragment
         */
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, NavigatorFragment.newInstance())
                .commit()
        }
    }

}