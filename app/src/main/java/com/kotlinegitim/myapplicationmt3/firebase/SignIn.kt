package com.kotlinegitim.myapplicationmt3.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class SignIn {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    fun signIn(email: String, password: String) : Boolean {
        Log.e("log girildi","!!")
        var state = false
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                state = true
            }
        }.addOnFailureListener { exception->
            Log.e("HATA",exception.message.toString())
            state = false
        }
        return state
    }
}