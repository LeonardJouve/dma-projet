package com.example.borne

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.borne.viewmodels.AttendanceViewModel
import com.example.borne.viewmodels.AttendanceViewModelFactory
import kotlin.getValue

/**
 * A fragment representing a list of Items.
 */
class UserItemFragment : Fragment() {

    private var columnCount = 1

    private val viewModel: AttendanceViewModel by activityViewModels {
        AttendanceViewModelFactory((requireActivity().application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                viewModel.getUsers().observe(viewLifecycleOwner) { users ->

                    // Ouvre l'activité HistoryActivity
                    adapter = UserAdapter(users) { position ->/*
                        val intent = Intent(requireContext(), HistoryActivity::class.java)
                        intent.putExtra("position", position)
                        startActivity(intent)*/
                    }
                }
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            UserItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}