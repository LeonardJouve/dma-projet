package com.example.borne

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels
import com.example.borne.database.models.User
import com.example.borne.viewmodels.AttendanceViewModel
import com.example.borne.viewmodels.AttendanceViewModelFactory

class HistoryActivity : AppCompatActivity() {

    private val viewModel: AttendanceViewModel by viewModels {
        AttendanceViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        @Suppress("DEPRECATION")
        val user = intent.getSerializableExtra("user") as User

        viewModel.getUserEventHistory(user).observe(this) { records ->
            val recyclerView = findViewById<RecyclerView>(R.id.list)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = HistoryAdapter(records)
        }
    }
}