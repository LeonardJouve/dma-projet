package com.example.myapplication

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack

class GgwaveManager {

    private external fun generateAudio(payload: String): ShortArray

    fun playTextByAudio(texte: String) {

        val audioData = generateAudio(texte)

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(48000)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build()
            )
            .setBufferSizeInBytes(audioData.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack.write(audioData, 0, audioData.size)

        audioTrack.play()
    }

    companion object {
        init {
            System.loadLibrary("ggwave_native")
        }
    }
}