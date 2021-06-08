package com.rick.chatapp.chat.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.rick.chatapp.R
import com.rick.chatapp.chat.model.User
import com.rick.chatapp.chat.ui.ChatActivity
import com.rick.chatapp.databinding.FragmentListofusersBinding
import com.rick.chatapp.databinding.ItemListofusersBinding
import com.rick.chatapp.util.Constants.USER
import com.rick.chatapp.util.Constants.USERS
import com.squareup.picasso.Picasso

class ListOfUsersFragment: Fragment() {

    private var _binding: FragmentListofusersBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemBinding: ItemListofusersBinding
    // get the current user and list of users
    private val currentUser = arguments?.getSerializable(USER) as User
    private val listUsers = arguments?.getSerializable(USERS) as MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListofusersBinding.inflate(inflater, container, false)
        // Set the adapter
        with(binding.listUsers){
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = ListOfUsersAdapter()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ChatActivity).supportActionBar?.title = "Users"
        (activity as ChatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listUsers.remove(currentUser)
        Log.d("list", "list: $listUsers")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    inner class ListOfUsersAdapter : RecyclerView.Adapter<ListOfUsersAdapter.ListOfUsersViewHolder>() {
        inner class ListOfUsersViewHolder(binding: ItemListofusersBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfUsersViewHolder {
            itemBinding = ItemListofusersBinding.inflate(LayoutInflater.from(parent.context))
            return ListOfUsersViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ListOfUsersViewHolder, position: Int) {
            with(holder){
                with(listUsers[position]){
                    itemBinding.name.text = this.name
                    itemBinding.status.text = this.status
                    Picasso.get().load(this.profilePicture)
                        .error(R.drawable.default_profile)
                        .into(itemBinding.profilePic)
                }
            }
        }

        override fun getItemCount() = listUsers.size
    }

}