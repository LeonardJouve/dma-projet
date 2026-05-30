package com.example.borne

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.example.borne.viewmodels.AttendanceViewModel
import com.example.borne.viewmodels.AttendanceViewModelFactory

class MainActivity : AppCompatActivity() {
    private val attendanceViewModel: AttendanceViewModel by viewModels {
        AttendanceViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, UserItemFragment.newInstance(1))
                .commit()
        }
    }
}