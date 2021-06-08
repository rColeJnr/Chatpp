package com.rick.chatapp.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.rick.chatapp.chat.model.User
import com.rick.chatapp.chat.ui.ChatActivity
import com.rick.chatapp.databinding.ActivityAuthBinding
import com.rick.chatapp.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.regex.Matcher
import java.util.regex.Pattern

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    val firebaseAuth = FirebaseAuth.getInstance()
    var user = User()
    val listUsers = mutableListOf<User>()
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

    fun requestAllUsers(): List<User> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = Firebase.firestore.collection("users").get().await()
                for (dc in querySnapshot.documentChanges){
                    when(dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val user = dc.document.toObject<User>()
                            Log.w(Constants.TAGLISTUSER, "user name: ${user.name}")
                            user?.let {
                                listUsers.add(user)
                                listUsers.toSet()
                                listUsers.sortBy{it.name}
                            }
                        }
                    }
                }
            } catch (e: FirebaseFirestoreException){
                Log.d(Constants.TAGLISTUSER, "failed to get users: ${e.printStackTrace()}")
            }
        }
        return listUsers
    }

    fun requestCurrentUser(): User {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = Firebase.firestore.collection("users")
                    .whereEqualTo("userId", ChatActivity.currentUser?.uid)
                    .get().await()
                // this query only contains one element which is the user whose id's matches our current
                user = querySnapshot.documents[0].toObject<User>()!!
            } catch (e: Exception){
                Log.d(Constants.TAGLISTUSER, "failed to get current user: ${e.printStackTrace()}")
            }
        }
        return user
    }



}