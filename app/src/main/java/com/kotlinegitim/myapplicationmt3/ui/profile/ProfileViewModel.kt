package com.kotlinegitim.myapplicationmt3.ui.profile

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val db = FirebaseFirestore.getInstance()

    fun getMoney(coin: TextView) {
        val currentUserUid=firebaseAuth.currentUser?.uid
        if (currentUserUid != null) {
            db.collection("users").document(currentUserUid).collection("money").document(currentUserUid+"money")
                .get().addOnSuccessListener {
                    val money = it.getDouble("money")
                    coin.text = money.toString()+"TL"
                }
        }
    }
    fun getUsername(usernametext: TextView){
        val currentUserUid=firebaseAuth.currentUser?.uid
        if (currentUserUid != null) {
            db.collection("users")
                .document(currentUserUid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val username = document.getString("username")
                        usernametext.text= username.toString()
                        if (username != null) {
                            println("Current username: $username")
                        }
                    } else {
                        println("Belirtilen UID ile ilgili belge bulunamadı.")
                    }
                }
                .addOnFailureListener { exception ->
                    println("Firestore'dan veri alınamadı: $exception")
                }
        } else {
            println("Oturum açmış bir kullanıcı bulunamadı.")
        }
    }
}