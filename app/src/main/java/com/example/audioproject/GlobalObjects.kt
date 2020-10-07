package com.example.audioproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName

object Tag{
    const val TAG = "soundscape-app"
}

object CategoryList{
    var categories = ArrayList<String>()
    init{
        categories.add("People")
        categories.add("Animals")
        categories.add("Traffic")
        categories.add("Weather")
    }
}

object Soundscapes{
    var soundscapes = ArrayList<Soundscape>()
    init{
        soundscapes = ArrayList<Soundscape>()
    }
}

object Soundlist{
    var sounds = ArrayList<DemoApi.Model.Sound>()
}

class SoundlistJson : ArrayList<Soundscape>()
