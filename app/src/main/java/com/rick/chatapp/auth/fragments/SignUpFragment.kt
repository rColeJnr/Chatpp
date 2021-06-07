 package com.rick.chatapp.auth.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.SetOptions
import com.rick.chatapp.R
import com.rick.chatapp.auth.AuthActivity
import com.rick.chatapp.auth.AuthActivity.Companion.isEmailValid
import com.rick.chatapp.auth.AuthActivity.Companion.isPasswordValid
import com.rick.chatapp.auth.AuthActivity.Companion.usersCollectionReference
import com.rick.chatapp.databinding.FragmentSignupBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpFragment: Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth = AuthActivity().firebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            signUpBtn.setOnClickListener {
                registerUser(
                    this.userName.editText?.text.toString(),
                    this.email.editText?.text.toString(),
                    this.password.editText?.text.toString())

            }

            signUpLoginTv.setOnClickListener {
                findNavController().navigate(
                    R.id.action_signUpFragment_to_signInFragment
                )
            }
        }

    }

    private fun registerUser(
        name: String,
        email: String,
        password: String
    ){
        if (isEmailValid(email) && isPasswordValid(password)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                    val newUserMap = hashMapOf(
                        "userId" to firebaseAuth.currentUser!!.uid,
                        "name" to name
                    )
                    usersCollectionReference.document(email + name)
                        .set(newUserMap, SetOptions.merge())
                    checkSignUpState()
                } catch (e: FirebaseAuthException){
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "Failed to register, try again", Toast.LENGTH_LONG).show()
                        Log.w("authregisterFailed", e.stackTrace.toString())
                    }
                }
            }
        }
    }

    private fun checkSignUpState() {
        if (firebaseAuth.currentUser != null){ // navigate to mainFragment
            findNavController().navigate(R.id.action_signUpFragment_to_chatActivity)
            activity?.finish()
        }
    }

    override fun onStart() {
        super.onStart()
        checkSignUpState()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}