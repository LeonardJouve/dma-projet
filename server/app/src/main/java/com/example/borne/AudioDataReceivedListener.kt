package com.example.borne

fun interface AudioDataReceivedListener {
    fun onAudioDataReceived(data: ShortArray)
}