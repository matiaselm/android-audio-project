package com.example.audioproject

object Tag{
    const val TAG = "soundscape-app"
}

object CategoryList{
    var categories = ArrayList<String>()
    init{
        categories.add("Traffic")
        categories.add("People")
        categories.add("Nature")
        categories.add("Cats")
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