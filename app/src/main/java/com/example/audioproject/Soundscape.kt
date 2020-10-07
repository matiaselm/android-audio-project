package com.example.audioproject

/**
 * soundscape with a name, a list of sounds and a list of volumes for those sounds
 */
class Soundscape(var name: String, var ssSounds: ArrayList<DemoApi.Model.Sound>, var volume: ArrayList<Float>)