package com.kotlinegitim.myapplicationmt3.ui.EditProfileInfos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileInfosViewModel : ViewModel() {

    var _isChange : MutableLiveData<Boolean> = MutableLiveData(false)

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val db = FirebaseFirestore.getInstance()
    val user = firebaseAuth.currentUser
    val uid = user?.uid

    fun SaveChanges(username: String, Mail: String, Password: CharSequence){

        val updateDataUserMap = hashMapOf(
            "username" to username,
            "email" to Mail,
            "password" to Password
        )
        if (uid != null) {
            db.collection("users").document(uid).set(updateDataUserMap)
                .addOnSuccessListener {
                    Log.e("firestora database","firestore update db success")
                _isChange.value = true
                }.addOnFailureListener {
                    Log.e("firestora database","firestore update db unsuccessfull")
                }
        }
    }
}