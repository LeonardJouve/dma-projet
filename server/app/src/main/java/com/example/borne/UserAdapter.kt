package com.example.borne

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.example.borne.placeholder.PlaceholderContent.PlaceholderItem
import com.example.borne.databinding.FragmentUserItemBinding

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class UserAdapter(
    private val values: List<UserItem>
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
        val item = values[position]
        holder.nameView.text = item.name
        holder.statusView.text = if (item.status) "Présent" else "Absent"

        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        holder.dateView.text = formatter.format(item.date)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.name
        val statusView: TextView = binding.status
        val dateView : TextView = binding.date

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'"
        }
    }
}