package com.example.audioproject

object Tag{
    const val TAG = "sound-app"
}

object CategoryList{
    var categories = ArrayList<String>()
    init{
        categories.add("Banjo")
        categories.add("People")
        categories.add("Cats")
        categories.add("Nature")
    }
}