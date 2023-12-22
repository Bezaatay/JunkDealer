package com.kotlinegitim.myapplicationmt3.ui.payment

import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PaymentViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val uid = firebaseAuth.currentUser?.uid.toString()
    private val db = FirebaseFirestore.getInstance()

    private var _isUploadMoney : MutableLiveData<Boolean> = MutableLiveData(false)
    fun getMoney(coin: TextView) {
        db.collection("users").document(uid).collection("money").document(uid+"money")
            .get().addOnSuccessListener {
                val money = it.getDouble("money").toString()
                coin.text = "$money TL"
            }
    }
    fun buyProduct(totalPaymentTxt: String, purseTxt: String) {
        val totalPayment = totalPaymentTxt.toDouble()
        val purse = purseTxt.toDouble()

        if(totalPayment > purse){
            //fragmenttaki alert çalışsın
        }
        else{
            //ödeme yapılsın cüzdandaki tutar güncellensin
        }
    }
}