package com.kotlinegitim.myapplicationmt3.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginFragment2ViewModel : ViewModel() {

    var _isLogin : MutableLiveData<Boolean> = MutableLiveData(false)

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun login(email: String, password: String){
        Log.e("log girildi","!!")
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                _isLogin.value=true
            }
        }.addOnFailureListener { exception->
            Log.e("HATA",exception.message.toString())
        }
    }
}