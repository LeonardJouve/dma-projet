package com.example.borne

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.borne.viewmodels.AttendanceViewModel
import com.example.borne.viewmodels.AttendanceViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.samstevens.totp.code.CodeGenerator
import dev.samstevens.totp.code.DefaultCodeGenerator
import dev.samstevens.totp.code.DefaultCodeVerifier
import dev.samstevens.totp.time.SystemTimeProvider
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        init {
            System.loadLibrary("ggwave")
        }
        val REQUEST_RECORD_AUDIO: Int = 13
    }
    private val attendanceViewModel: AttendanceViewModel by viewModels {
        AttendanceViewModelFactory((application as App).repository)
    }

    private lateinit var mCapturingThread: CapturingThread

    private external fun initNative()
    private external fun processCaptureData(data: ShortArray)

    private fun onNativeReceivedMessage(c_message: ByteArray) {
        val digitBytes = c_message.takeWhile {
            it.toInt().toChar().isDigit()
        }

        val message = String(digitBytes.toByteArray())
        if (message.length <= 6) {
            runOnUiThread {
                Toast.makeText(this, "Received message: $message", Toast.LENGTH_SHORT).show()
            }
            return
        }

        val totp: String = message.substring(0, 6)
        Log.d("totp", "TOTP: $totp")
        val userId: Long = message.substring(6).toLongOrNull() ?: return
        Log.d("totp", "User ID: $userId")

        val user = attendanceViewModel.getUser(userId) ?: return

        val timeProvider = SystemTimeProvider()
        val codeGenerator: CodeGenerator = DefaultCodeGenerator()
        val verifier = DefaultCodeVerifier(codeGenerator, timeProvider)

        if (!verifier.isValidCode(user.secret, totp)) {
            runOnUiThread {
                Toast.makeText(this, "invalid totp", Toast.LENGTH_SHORT).show()
            }
            return
        }

        runOnUiThread {
            Toast.makeText(this, "valid totp", Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            attendanceViewModel.badge(user)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNative()
        mCapturingThread = CapturingThread({ data ->
            processCaptureData(data)
        })
        startAudioCapture()

        if (!microphoneSupportUltrasound()) {
            Toast.makeText(this, "phone microphone does not support ultrasound", Toast.LENGTH_SHORT).show()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, UserItemFragment.newInstance(1))
                .commit()
        }

        val button = findViewById<FloatingActionButton>(R.id.create_user)

        button.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCapturingThread.stopCapturing()
    }

    private fun startAudioCapture() {
        Log.i("ggwave", "startAudioCapturingSafe")

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            Log.i("ggwave", " - record permission granted")
            mCapturingThread.startCapturing()
        } else {
            Log.i("ggwave", " - record permission NOT granted")
            requestMicrophonePermission()
        }
    }

    private fun microphoneSupportUltrasound(): Boolean {
        val audioManager = this.getSystemService(AUDIO_SERVICE) as AudioManager
        return audioManager.getProperty(AudioManager.PROPERTY_SUPPORT_MIC_NEAR_ULTRASOUND) == "true"
    }

    private fun requestMicrophonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("Microphone Access Requested")
                .setMessage("Microphone access is required in order to receive audio messages")
                .setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf<String>(Manifest.permission.RECORD_AUDIO),
                            REQUEST_RECORD_AUDIO
                        )
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf<String>(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCapturingThread.startCapturing()
        }
    }
}