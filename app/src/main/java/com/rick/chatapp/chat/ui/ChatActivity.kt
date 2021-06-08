package com.rick.chatapp.chat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.rick.chatapp.auth.AuthActivity
import com.rick.chatapp.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    companion object{
        val currentUser = FirebaseAuth.getInstance().currentUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser?.let {
            AuthActivity().requestAllUsers()
            AuthActivity().requestCurrentUser()
        }

    }
}





















