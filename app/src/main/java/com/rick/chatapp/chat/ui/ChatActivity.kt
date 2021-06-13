package com.rick.chatapp.chat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.google.firebase.auth.FirebaseAuth
import com.rick.chatapp.auth.AuthActivity
import com.rick.chatapp.chat.model.User
import com.rick.chatapp.databinding.ActivityChatBinding
import com.rick.chatapp.util.Constants.USER
import com.rick.chatapp.util.Constants.USERS

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    companion object{
        val currentUser = FirebaseAuth.getInstance().currentUser
        lateinit var user: User
        lateinit var users: MutableList<User>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser?.let {
            user = AuthActivity().requestCurrentUser()
            users = AuthActivity().requestAllUsers()
        }

        val intent = Intent()
        val bundle = bundleOf(USERS to users,
            USER to user)
        intent.putExtras(bundle)

    }
}





















