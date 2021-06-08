package com.rick.chatapp.chat.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.rick.chatapp.R
import com.rick.chatapp.chat.model.ChatListMessage
import com.rick.chatapp.chat.ui.ChatActivity
import com.rick.chatapp.chat.ui.ChatActivity.Companion.currentUser
import com.rick.chatapp.databinding.FragmentListofchatsBinding
import com.rick.chatapp.databinding.ItemListofchatsBinding
import com.rick.chatapp.util.Constants.TAGLISTCHATS
import com.squareup.picasso.Picasso

/**
 * A fragment representing a list of Items.
 */
class ListOfChatsFragment : Fragment() {

    private var _binding: FragmentListofchatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemBinding: ItemListofchatsBinding
    private val messageList = mutableListOf<ChatListMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListofchatsBinding.inflate(inflater, container, false)
        // Set the adapter
        with(binding.list){
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = ListOfChatsAdapter()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ChatActivity).supportActionBar?.title = "List of Chats"
        (activity as ChatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        listenForLatestMessages()
    }

    // Query for new messages in firestore
    private fun listenForLatestMessages() {
        val sender = currentUser?.uid
        Firebase.firestore.collection("latest/messages/$sender")
            .orderBy("messageTime").addSnapshotListener { querySnapshot, error ->

                error?.let {
                    Log.w(TAGLISTCHATS, "error receiving messages: ${error.message}")
                    return@addSnapshotListener
                }

                messageList.clear()
                querySnapshot?.let {
                    for (dc in it.documentChanges) {
                        val chat = dc.document.toObject<ChatListMessage>()
                        messageList.add(0, chat)
                    }
                }
                ListOfChatsAdapter().listOfChatsDiffer.submitList(messageList.toList())
                ListOfChatsAdapter().notifyDataSetChanged()
            }
    }

    private inner class ListOfChatsAdapter:
        RecyclerView.Adapter<ListOfChatsAdapter.ListOfChatsViewHolder>(){
            inner class ListOfChatsViewHolder(binding: ItemListofchatsBinding):
                RecyclerView.ViewHolder(binding.root)

        private val listOfChatsDiffUtil = object: DiffUtil.ItemCallback<ChatListMessage>(){
            override fun areItemsTheSame(
                oldItem: ChatListMessage,
                newItem: ChatListMessage
            ): Boolean {
                return oldItem.messageTime == newItem.messageTime
            }

            override fun areContentsTheSame(
                oldItem: ChatListMessage,
                newItem: ChatListMessage
            ): Boolean {
                return oldItem.messageText == newItem.messageText
            }
        }

        val listOfChatsDiffer = AsyncListDiffer(this, listOfChatsDiffUtil)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfChatsViewHolder {
            itemBinding = ItemListofchatsBinding.inflate(LayoutInflater.from(parent.context))
            return ListOfChatsViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ListOfChatsViewHolder, position: Int) {
            with(holder){
                with(listOfChatsDiffer.currentList[position]){
                    if (this.messageReceiverId == currentUser?.uid){
                        itemBinding.name.text = this.nameSender
                        Picasso.get().load(this.profilePictureSender)
                            .error(R.drawable.default_profile)
                            .into(itemBinding.profilePic)
                    } else {
                        itemBinding.name.text = this.nameReceiver
                        Picasso.get().load(this.profilePictureReceiver)
                            .error(R.drawable.default_profile)
                            .into(itemBinding.profilePic)
                    }
                }
            }
        }

        override fun getItemCount() = listOfChatsDiffer.currentList.size
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