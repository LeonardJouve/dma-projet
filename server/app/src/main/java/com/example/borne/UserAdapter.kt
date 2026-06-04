package com.example.borne

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.borne.database.models.User
import com.example.borne.database.models.UserWithLastEvent
import com.example.borne.databinding.FragmentUserItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * [RecyclerView.Adapter] that can display a [User].
 */
class UserAdapter(
    private val values: List<UserWithLastEvent>,
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
        val user = values[position].user
        val lastEvent= values[position].attendanceEvent

        holder.nameView.text = user.name

        if (lastEvent == null) {
            holder.statusView.text = "None"
            holder.dateView.text = "None"
        } else {
            holder.statusView.text = lastEvent.type.toString()
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            holder.dateView.text = formatter.format(lastEvent.time)
        }

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