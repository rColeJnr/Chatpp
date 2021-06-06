package com.rick.chatapp.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rick.chatapp.R
import com.rick.chatapp.databinding.ActivityAuthBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    val firebaseAuth = FirebaseAuth.getInstance()
    companion object {
        val usersCollectionReference = Firebase.firestore.collection("users")

        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPasswordValid(password: String): Boolean {
            val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"

            val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)
            val matcher: Matcher = pattern.matcher(password)

            return matcher.matches()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



}