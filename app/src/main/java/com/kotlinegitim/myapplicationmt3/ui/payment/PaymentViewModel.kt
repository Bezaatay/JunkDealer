package com.kotlinegitim.myapplicationmt3.ui.payment

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
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

    private val _notEnoughMoneyDialog = MutableLiveData<Boolean>()
    val notEnoughMoneyDialog: LiveData<Boolean> get() = _notEnoughMoneyDialog

    private val _enoughMoneyDialog = MutableLiveData<Boolean>()
    val enoughMoneyDialog: LiveData<Boolean> get() = _enoughMoneyDialog

    private val money = db.collection("users").document(uid).collection("money").document(uid+"money")
    private val currentUserCollection =  db.collection("users").document(uid)

    fun getMoney(coin: TextView) {
        money.get().addOnSuccessListener {
                val money = it.getDouble("money").toString()
                coin.text = "$money TL"
            }
    }
    fun buyProduct(
        totalPaymentTxt: String,
        purseTxt: String,
        photoUrl: String,
        category: String,
        sellerUsername: String,
        description: String
    ) {
        val numericValue = totalPaymentTxt.replace("[^\\d.]".toRegex(), "")
        val numericValuePurse = purseTxt.replace("[^\\d.]".toRegex(), "")

        // Elde edilen sayısal değeri double'a çevirme
        val totalPaymentDbl = numericValue.toDouble()
        val purseDbl = numericValuePurse.toDouble()

        if(totalPaymentDbl > purseDbl){
            notEnoughMoney()
        }
        else{
            moneyInPurse(totalPaymentDbl)
            productIBought(totalPaymentDbl,photoUrl,category,sellerUsername)
            productSellerSold(sellerUsername,photoUrl,category,description,totalPaymentDbl)
            deleteInProducts(photoUrl)
            enoughMoney()
        }
    }
    private fun notEnoughMoney() {
        _notEnoughMoneyDialog.value = true
    }
    private fun enoughMoney() {
        _enoughMoneyDialog.value = true
    }
    fun notEnoughMoneyDialogShown() {
        _notEnoughMoneyDialog.value = false
    }
    fun enoughMoneyDialogShown() {
        _enoughMoneyDialog.value = false
    }
    private fun moneyInPurse(totalPaymentDbl: Double) {
        money.get()
            .addOnSuccessListener { document ->
                val currentMoney = document.getDouble("money") ?: 0.0
                val newMoney = currentMoney - totalPaymentDbl

                val updatedTotalMoney = mapOf(
                    "money" to newMoney
                )

                money
                    .update(updatedTotalMoney)
                    .addOnSuccessListener {
                        // Handle success if needed
                    }
            }
    }
    private fun productIBought(
        totalPaymentTxt: Double,
        photoUrl: String,
        category: String,
        sellerUsername: String
    ) {
        val productIBoughtMap = hashMapOf(
            "productPhotoUrl" to photoUrl,
            "category" to category,
            "discounted-price" to totalPaymentTxt,
            "seller-username" to sellerUsername
        )
        currentUserCollection.collection("ProductsIBought").add(productIBoughtMap)
            .addOnSuccessListener {
                Log.e("productIBought","basariyla yuklendi")
            }.addOnFailureListener {
                Log.e("productIBought","yükleme başarısız")
            }
    }
    private fun productSellerSold(
        sellerUsername: String,
        photoUrl: String,
        category: String,
        description: String,
        totalPaymentDbl: Double
    ) {
        db.collection("users")
            .get()
            .addOnSuccessListener { usersDocuments ->
                for (userDocument in usersDocuments) {
                    val usernameDb = userDocument.getString("username").toString()

                    if (sellerUsername == usernameDb) {
                        val sellerMail = userDocument.getString("email")

                        // ProductsISold koleksiyonuna ürün ekleme
                        val productISoldMap = hashMapOf(
                            "sellerMail" to sellerMail,
                            "productPrice" to totalPaymentDbl,
                            "category" to category,
                            "description" to description,
                            "photoUrl" to photoUrl
                        )
                        userDocument.reference
                            .collection("ProductsISold")
                            .add(productISoldMap)
                            .addOnSuccessListener {
                                Log.e("productISold", "Başarıyla eklendi")
                            }
                            .addOnFailureListener {
                                Log.e("productISold", "Eklenirken hata oluştu: $it")
                            }

                        // ProductsIUpload koleksiyonunu silme
                        userDocument.reference
                            .collection("ProductsIUpload")
                            .whereEqualTo("Product-Url", photoUrl)
                            .get()
                            .addOnSuccessListener { productsIUploadDocuments ->
                                for (productIUploadDocument in productsIUploadDocuments) {
                                    productIUploadDocument.reference.delete()
                                        .addOnSuccessListener {
                                            Log.e("productIUpload", "Başarıyla silindi")
                                        }
                                        .addOnFailureListener {
                                            Log.e("productIUpload", "Silinirken hata oluştu: $it")
                                        }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("productIUpload", "Belgeleri alırken hata oluştu: $exception")
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("users", "Kullanıcıları alırken hata oluştu: $exception")
            }
    }

    fun deleteInProducts(photoUrl: String) {
        db.collection("products")
            .whereEqualTo("Product-Url", photoUrl)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    document.reference.delete()
                        .addOnSuccessListener {
                            Log.d("DeleteProduct", "Belge başarıyla silindi")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("DeleteProduct", "Belge silinirken hata oluştu: $exception")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("DeleteProduct", "Belgeleri alırken hata oluştu: $exception")
            }
    }
}
