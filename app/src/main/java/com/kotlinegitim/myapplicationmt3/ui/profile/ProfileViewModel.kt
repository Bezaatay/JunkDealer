package com.kotlinegitim.myapplicationmt3.ui.profile

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    var _isPurseAccount : MutableLiveData<Boolean> = MutableLiveData(false)

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val db = FirebaseFirestore.getInstance()
    private val uid = firebaseAuth.currentUser?.uid.toString()

    fun createMoneyDb() {

        val userDocRef = uid.let { db.collection("users").document(it) }
        val moneyCollectionRef = userDocRef.collection("money")

        moneyCollectionRef.document(uid + "money").set(mapOf("money" to 0.0))
            .addOnSuccessListener {
                Log.e("success", "money depolama basarili")
                if (it != null) {
                    _isPurseAccount.value = true
                }
            }
            .addOnFailureListener {
                Log.e("success", "money depolama basarisiz")
            }
    }
    fun getMoney(coin: TextView) {
        if (uid != null) {
            db.collection("users").document(uid).collection("money").document(uid+"money")
                .get().addOnSuccessListener {
                    val money = it.getDouble("money")
                    coin.text = money.toString()+"TL"
                }
        }
    }
    fun getUsername(usernametext: TextView) {
            val currentUserUid = firebaseAuth.currentUser?.uid
            if (currentUserUid != null) {
                db.collection("users")
                    .document(currentUserUid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val username = document.getString("username")
                            usernametext.text = username.toString()
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