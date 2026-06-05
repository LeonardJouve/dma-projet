package com.example.borne

import android.Manifest
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import android.util.Log
import androidx.annotation.RequiresPermission


class CapturingThread(val mListener: AudioDataReceivedListener) {
    companion object {
        val LOG_TAG: String = CapturingThread::class.java.getSimpleName()
        val SAMPLE_RATE: Int = 48000
    }

    private var mShouldContinue = false
    private var mThread: Thread? = null

    fun capturing(): Boolean {
        return mThread != null
    }

    fun startCapturing() {
        if (mThread != null) return

        mShouldContinue = true
        mThread = Thread(object : Runnable {
            @RequiresPermission(Manifest.permission.RECORD_AUDIO)
            override fun run() {
                capture()
            }
        })
        mThread!!.start()
    }

    fun stopCapturing() {
        if (mThread == null) return

        mShouldContinue = false
        mThread = null
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun capture() {
        Log.v(LOG_TAG, "Start")
        Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)

        // buffer size in bytes
        var bufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = SAMPLE_RATE * 2
        }
        bufferSize = 4 * 1024

        val audioBuffer = ShortArray(bufferSize / 2)

        val record = AudioRecord(
            MediaRecorder.AudioSource.DEFAULT,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        Log.d("ggwave", "buffer size = " + bufferSize)
        Log.d("ggwave", "Sample rate = " + record.getSampleRate())

        if (record.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(LOG_TAG, "Audio Record can't initialize!")
            return
        }
        record.startRecording()

        Log.v(LOG_TAG, "Start capturing")

        var shortsRead: Long = 0
        while (mShouldContinue) {
            val numberOfShort = record.read(audioBuffer, 0, audioBuffer.size)
            shortsRead += numberOfShort.toLong()

            mListener.onAudioDataReceived(audioBuffer)
        }

        record.stop()
        record.release()

        Log.v(LOG_TAG, String.format("Capturing stopped. Samples read: %d", shortsRead))
    }
}