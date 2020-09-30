package com.example.audioproject

object Tag{
    const val TAG = "sound-app"
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