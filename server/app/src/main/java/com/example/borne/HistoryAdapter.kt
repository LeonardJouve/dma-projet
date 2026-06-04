package com.example.borne

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.borne.database.models.AttendanceEvent
import com.example.borne.databinding.FragmentHistoryItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * [RecyclerView.Adapter] that can display a [AttendanceEvent].
 */
class HistoryAdapter(
    private val values: List<AttendanceEvent>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentHistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = values[position]

        holder.statusView.text = event.type.toString()

        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        holder.dateView.text = formatter.format(event.time)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val statusView: TextView = binding.status
        val dateView : TextView = binding.date
    }
}