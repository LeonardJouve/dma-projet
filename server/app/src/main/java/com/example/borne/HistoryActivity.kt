package com.example.borne

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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

            val headers = findViewById<LinearLayout>(R.id.headers)
            val recyclerView = findViewById<RecyclerView>(R.id.list)
            val emptyMessage = findViewById<TextView>(R.id.empty_message)

            recyclerView.layoutManager = LinearLayoutManager(this)

            if (records.isEmpty()) {
                headers.visibility = View.GONE
                recyclerView.visibility = View.GONE
                emptyMessage.visibility = View.VISIBLE

            } else {
                headers.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
                emptyMessage.visibility = View.GONE

                recyclerView.adapter = HistoryAdapter(records)
            }
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