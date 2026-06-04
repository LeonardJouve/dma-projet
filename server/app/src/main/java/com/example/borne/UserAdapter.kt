package com.example.borne

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.borne.database.models.EventType
import com.example.borne.database.models.User

import com.example.borne.databinding.FragmentUserItemBinding

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * [RecyclerView.Adapter] that can display a [User].
 */
class UserAdapter(
    private val values: List<User>,
    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentUserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = values[position]
        holder.nameView.text = user.name
/*
        val lastEvent = viewModel.getUserLastEvent(item)
        holder.statusView.text = if (lastEvent.type == EventType.IN) "Présent" else "Absent"

        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        holder.dateView.text = formatter.format(lastEvent.time)
*/

        holder.statusView.text = "STATUS"
        holder.dateView.text = "DATE"
        holder.itemView.setOnClickListener { onItemClick(user) }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.name
        val statusView: TextView = binding.status
        val dateView : TextView = binding.date
    }
}