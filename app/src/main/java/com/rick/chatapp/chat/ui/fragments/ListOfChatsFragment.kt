package com.rick.chatapp.chat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rick.chatapp.R
import com.rick.chatapp.databinding.FragmentListOfChatsBinding
import com.rick.chatapp.databinding.ItemListOfChatsBinding

/**
 * A fragment representing a list of Items.
 */
class ListOfChatsFragment : Fragment() {

    private var _binding: ItemListOfChatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemBiding: FragmentListOfChatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemListOfChatsBinding.inflate(inflater, container, false)
        // Set the adapter
        with(binding.list){
            layoutManager = LinearLayoutManager(context)
            adapter = ListOfChatsAdapter()
        }
        return binding.root
    }

    private inner class ListOfChatsAdapter:
        RecyclerView.Adapter<ListOfChatsAdapter.ListOfChatsViewHolder>(){
            inner class ListOfChatsViewHolder(binding: FragmentListOfChatsBinding):
                RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfChatsViewHolder {

        }

        override fun onBindViewHolder(holder: ListOfChatsViewHolder, position: Int) {

        }

        override fun getItemCount() = 1
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ListOfChatsFragment()
    }
}