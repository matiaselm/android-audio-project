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
    lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    /*    val navController = findNavController(R.id.nav_controller_view_tag)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
*/
        val sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE) ?: return
        val value = sharedPref.getString(Tag.TAG, "null")
        Log.d("sharedpref", value!!)
        var ss = Gson().fromJson<SoundlistJson>(value, SoundlistJson::class.java)
        if(ss != null) {
            Soundscapes.soundscapes = ss
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, NavigatorFragment.newInstance())
                .commit()
        }
    }
/*
    override fun onSupportNavigateUp(): Boolean {

        // Handle the back button event and return true to override
        // the default behavior the same way as the OnBackPressedCallback.
        // TODO(reason: handle custom back behavior here if desired.)

        // If no custom behavior was handled perform the default action.
        val navController = findNavController(R.id.nav_controller_view_tag)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

 */
}