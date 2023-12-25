package com.kotlinegitim.myapplicationmt3.ui.shopping

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ShoppingViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val uid = firebaseAuth.currentUser?.uid.toString()
    private val db = FirebaseFirestore.getInstance()
    private val currentUserCollection =  db.collection("users").document(uid)

    fun getPurchases(){

    }
}