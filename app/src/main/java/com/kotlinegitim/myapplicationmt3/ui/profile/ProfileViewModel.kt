package com.kotlinegitim.myapplicationmt3.ui.profile

import android.util.Log
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
    private val db = FirebaseFirestore.getInstance()

    private fun setMoney(uid: String) {
        val userDocRef = uid.let { db.collection("users").document(it) }

        // "money" koleksiyonunu oluşturun veya varsa alın
        val moneyCollectionRef = userDocRef.collection("money").document(uid + "money")

        val moneyValues = hashMapOf(
            "money" to "0 TL"
        )
        moneyCollectionRef.set(moneyValues)
            ?.addOnSuccessListener {
                Log.e("success", "money depolama basairili")
            }
            ?.addOnFailureListener {
                Log.e("success", "money depolama basarisiz")
            }
    }

    fun getMoney(coin: TextView) {
        val currentUserUid = firebaseAuth.currentUser?.uid
        if (currentUserUid != null) {
            db.collection("users").document(currentUserUid).collection("money")
                .document(currentUserUid + "money")
                .get().addOnSuccessListener {
                    val money = it.getDouble("money")
                    if (money != null) {
                        coin.text = money.toString() + "TL"
                    } else {
                        Log.e("getmoney profile viewmodel", "money bos")
                    }

        }}}
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