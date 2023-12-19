package com.kotlinegitim.myapplicationmt3.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

    var _isSignin : MutableLiveData<Boolean> = MutableLiveData(false)
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val db = FirebaseFirestore.getInstance()

    fun checkEmailVerificationStatus() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            user.reload().addOnCompleteListener { reloadTask ->
                if (reloadTask.isSuccessful) {

                    if (user.isEmailVerified) {
                        Log.e("email doğrulandı", "E-posta başarıyla doğrulandı.")
                        _isSignin.value = true
                    } else {
                        Log.e("email doğrulanmadı", "E-posta henüz doğrulanmadı.")
                    }
                } else {
                    Log.e("reload", "Kullanıcı bilgileri güncellenemedi: ${reloadTask.exception?.message}")
                }
            }
        } else {
            Log.e("user", "Kullanıcı bilgileri alınamadı.")
        }
    }

    fun signup(email: String,username: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                val uid = user?.uid
                if (uid != null) {
                    user.sendEmailVerification().addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.e("email yollandı","please check email for verification")
                            _isSignin.value = true
                            saveToFirestore(uid,username,email,password)
                        }else{
                            Log.e("email yollanamadı","please check email for verification")
                        }
                    }.addOnFailureListener {
                        Log.e("email","email yollanamadı")
                    }
                } else {
                    Log.e("uid","uid alınamadı")
                }
            }
        }.addOnFailureListener {
        }
    }

    private fun saveToFirestore(uid:String, username: String, email: String, password: String) {
        val userMap = hashMapOf(
            "username" to username,
            "email" to email,
            "password" to password
        )
        db.collection("users").document(uid).set(userMap)
            .addOnSuccessListener {
                Log.e("firestora database","firestore db success")
            }.addOnFailureListener {
                Log.e("firestora database","firestore db unsuccessfull")
            }
    }

    fun passwordController(password:String,comfirmpassword:String){
        if (password==comfirmpassword){
            Log.e("şifre","eşleşti")
        }else{
            Log.e("şifre","hatası")
        }
    }
    fun getUsername(username:String){
        Log.e("username is",username)
    }
}