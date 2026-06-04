package com.example.borne

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.borne.database.models.User
import com.example.borne.viewmodels.AttendanceViewModel
import com.example.borne.viewmodels.AttendanceViewModelFactory
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import dev.samstevens.totp.secret.DefaultSecretGenerator
import kotlinx.coroutines.launch
import kotlin.getValue

class CreateUserActivity : AppCompatActivity() {
    private val secretGenerator = DefaultSecretGenerator();
    private val attendanceViewModel: AttendanceViewModel by viewModels {
        AttendanceViewModelFactory((application as App).repository)
    }

    private fun generateUserQR(user: User): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap("id=${user.id},name=${user.name},secret=${user.secret}", BarcodeFormat.QR_CODE, 400, 400)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val createButton = findViewById<Button>(R.id.createButton)
        val backButton = findViewById<Button>(R.id.backButton)
        val qrImage = findViewById<ImageView>(R.id.qrImage)

        backButton.setOnClickListener {
            finish()
        }

        createButton.setOnClickListener {
            val name = nameInput.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: send to ViewModel / database
            val user = User(name=name, secret=secretGenerator.generate())
            lifecycleScope.launch {
                val id = attendanceViewModel.insertUser(user)
                user.id = id

                qrImage.setImageBitmap(generateUserQR(user))
                qrImage.visibility = View.VISIBLE
                Toast.makeText(this@CreateUserActivity, "Created: $name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}