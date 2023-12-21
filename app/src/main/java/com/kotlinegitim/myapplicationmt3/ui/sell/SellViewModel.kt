package com.kotlinegitim.myapplicationmt3.ui.sell

import android.net.Uri
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class SellViewModel : ViewModel() {
    var _isUpload : MutableLiveData<Boolean> = MutableLiveData(false)

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
   private val db = FirebaseFirestore.getInstance()
   private val currentUserUid = firebaseAuth.currentUser?.uid
   private val storageRef = FirebaseStorage.getInstance().reference


    fun getLocation(latitudeValue: TextView, longitudeValue: TextView) {
        if(currentUserUid != null) {
            db.collection("users").document(currentUserUid).collection("location").document(currentUserUid+"location")
                .get().addOnSuccessListener { documentSnapshot ->
                    val latitude = documentSnapshot.getDouble("latitude")
                    val longitude = documentSnapshot.getDouble("longitude")

                    if (latitude != null && longitude != null) {
                      latitudeValue.text= latitude.toString()
                        longitudeValue.text= longitude.toString()
                    }
                    else {
                        // Eğer latitude veya longitude değeri null ise, hatayı log'a yazabilirsiniz
                        Log.e("PersonelInfosViewModel", "Latitude veya longitude değeri null.")
                    }
                }
        }
    }

    fun getMail(mailTxt: TextView) {
            val currentUserUid = firebaseAuth.currentUser?.uid
            if (currentUserUid != null) {
                db.collection("users")
                    .document(currentUserUid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val email = document.getString("email")
                            mailTxt.text = email.toString()
                            if (email != null) {
                                println("Current Mail: $email")
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

    // Seçilen görseli Firebase Storage'a kaydetme
   fun uploadUrl(
        selectedImageUri: Uri?,
        productCategories: String,
        productDescription: String,
        productPrize: String,
        sellerMail: String,
        productLocationLatitude: String,
        productLocationLongitude: String,
    ) {
        Log.e("uri","$selectedImageUri")
        if(selectedImageUri != null) {

            val user = FirebaseAuth.getInstance().currentUser
            val uuid = UUID.randomUUID()
            val imageID = "${uuid}.jpg"
            val imageRef = storageRef.child("productsPhotos/${user?.email}/myProducts/$imageID")
            imageRef.putFile(selectedImageUri).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    val latitude = productLocationLatitude.toDouble()
                    val longitude = productLocationLongitude.toDouble()
                    uploadProduct(
                        productCategories,
                        productDescription,
                        productPrize,
                        downloadUrl,
                        sellerMail,
                        latitude,
                        longitude
                    )
                }.addOnFailureListener {
                    Log.e("hata url1-SellVM", it.toString())
                }
            }.addOnFailureListener{exception ->
                Log.e("hata url2-SellVM",exception.toString())
            }
        }
        Log.e("selected uri hata ","$selectedImageUri")
   }

    private fun uploadProduct(
        productCategories: String,
        productDescription: String,
        productPrize: String,
        productUrl: String,
        sellerMail: String,
        productLocationLatitude: Double,
        productLocationLongitude: Double
    ){
        val productMap = hashMapOf(
            "Product-Categories" to productCategories,
            "Product-Description" to productDescription,
            "Product-Prize" to productPrize+"TL",
            "Product-Url" to productUrl,
            "Product-Latitude" to productLocationLatitude,
            "Product-Longitude" to productLocationLongitude,
            "Seller-Mail" to sellerMail
         )
        if (currentUserUid != null) {
            db.collection("products").document().set(productMap)
                .addOnSuccessListener {
                    Log.e("products firestora database","firestore db success")
                }.addOnFailureListener {
                    Log.e("products firestora database","firestore db unsuccessfull")
                }
            uploadUserProduct(currentUserUid,productCategories, productDescription, productPrize,productUrl)
        }
    }

    private fun uploadUserProduct(currentUser:String, editTextProductCategories:String, editTextProductDescription:String, editTextProductPrize:String, productUrl : String){
        val userProductMap = hashMapOf(
            "Product-Categories" to editTextProductCategories,
            "Product-Description" to editTextProductDescription,
            "Product-Prize" to editTextProductPrize+"TL",
            "Product-Url" to productUrl
        )
        db.collection("users").document(currentUser).collection("ProductsIUpload").add(userProductMap)
        _isUpload.value = true
    }
}