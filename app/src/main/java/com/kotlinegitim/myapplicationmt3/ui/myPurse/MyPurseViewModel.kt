package com.kotlinegitim.myapplicationmt3.ui.myPurse

import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyPurseViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val db = FirebaseFirestore.getInstance()
    private val currentUserUid=firebaseAuth.currentUser?.uid

    fun getMoney(coin: TextView) {
        if (currentUserUid != null) {
            db.collection("users").document(currentUserUid).collection("money").document(currentUserUid+"money")
                .get().addOnSuccessListener {
                    val money = it.getDouble("money")
                    coin.text = money.toString()+"TL"
                }
        }
    }
    fun loadMoney(editTextText: EditText) {
        val newMoney = editTextText.text.toString().toDoubleOrNull() ?: 0.0

        if (currentUserUid != null) {
            val moneyDocument = db.collection("users")
                .document(currentUserUid)
                .collection("money")
                .document(currentUserUid + "money")

            moneyDocument.get()
                .addOnSuccessListener { document ->
                    val currentMoney = document.getDouble("money") ?: 0.0
                    val newTotalMoney = currentMoney + newMoney

                    val updatedTotalMoney = mapOf(
                        "money" to newTotalMoney
                    )

                    moneyDocument
                        .update(updatedTotalMoney)
                        .addOnSuccessListener {
                            // Handle success if needed
                        }
                }
        }
    }


}

