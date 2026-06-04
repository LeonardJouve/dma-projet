package com.example.borne

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.borne.database.models.User
import com.example.borne.viewmodels.AttendanceViewModel
import com.example.borne.viewmodels.AttendanceViewModelFactory
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private val attendanceViewModel: AttendanceViewModel by viewModels {
        AttendanceViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        @Suppress("DEPRECATION")
        val user = intent.getSerializableExtra("user") as User

        attendanceViewModel.getUserEventHistory(user).observe(this) { records ->
            val recyclerView = findViewById<RecyclerView>(R.id.list)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = HistoryAdapter(records)
        }

        val testButton = findViewById<Button>(R.id.testButton)
        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        // TODO : for test only
        testButton.setOnClickListener {
            lifecycleScope.launch {
                attendanceViewModel.badge(user)
            }
        }
    }
}