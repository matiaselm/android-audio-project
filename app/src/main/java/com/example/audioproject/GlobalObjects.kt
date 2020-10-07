package com.example.audioproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName

/**
 * tag for logging and such operations
 */
object Tag{
    const val TAG = "soundscape-app"
}

/**
 * list of categories for CategorySearchFragment
 */
object CategoryList{
    var categories = ArrayList<String>()
    init{
        categories.add("People")
        categories.add("Animals")
        categories.add("Traffic")
        categories.add("Weather")
    }
}

/**
 * Global list of soundscapes
 */
object Soundscapes{
    var soundscapes = ArrayList<Soundscape>()
    init{
        soundscapes = ArrayList<Soundscape>()
    }
}

/**
 * list of sounds while creating a soundscape
 */
object Soundlist{
    var sounds = ArrayList<DemoApi.Model.Sound>()
}

/**
 * class for converting json back to an object from shared preferences
 */
class SoundlistJson : ArrayList<Soundscape>()

fun formatResult(result: String):String{
    return result.dropLast(4)
}
